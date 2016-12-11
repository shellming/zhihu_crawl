package com.shellming.zhihu.services;

import com.shellming.zhihu.Config;
import com.shellming.zhihu.dao.AnswerDao;
import com.shellming.zhihu.dao.MetaDao;
import com.shellming.zhihu.dao.UserDao;
import com.shellming.zhihu.models.Answer;
import com.shellming.zhihu.models.User;
import com.shellming.zhihu.utils.ZhiHuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by ruluo1992 on 10/4/2016.
 */
@Service
@Profile("!test")
public class CrawlService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private ThreadPoolExecutor fixedExecutor;

    @Autowired
    ProgressService progressService;

    private static final Integer BATCH_SIZE = 100;

    static class GetAnswerTask implements Callable<List<Answer>> {
        private User user;
        private Long from;

        public GetAnswerTask(User user, Long from) {
            this.user = user;
            this.from = from;
        }

        @Override
        public List<Answer> call() throws Exception {
            List<Answer> answers = ZhiHuUtil.getAnswersFromUser(user, from);
            return answers;
        }
    }

    @Scheduled(fixedDelay = 2000)
    public void startAnswer() throws InterruptedException, ExecutionException, IOException {
        Integer lastId = progressService.getLastCheckedUserId();
        System.out.println("get answers from user " + lastId);
        List<User> users = userDao.getUsersById(lastId, BATCH_SIZE);
        List<Future<List<Answer>>> futures = new ArrayList<>(BATCH_SIZE);
        if (users.size() == 0) {
//            progressService.setLastCheckedUserId(0);
            return;
        } else {
            for (User user : users) {
                Answer lastAnswer = answerDao.getLastByUserUrl(user.url);
                Long fromTime = Config.answerFrom.getTime();
                if (lastAnswer != null) {
                    fromTime = lastAnswer.createTime;
                }
                Future<List<Answer>> answers = fixedExecutor.submit(new GetAnswerTask(user, fromTime));
                futures.add(answers);
            }
            int failCount = 0;
            for (Future<List<Answer>> future : futures) {
                try {
                    List<Answer> answers = future.get();
                    for (Answer answer : answers) {
                        if (!answerDao.isExistUrl(answer.url)) {
                            answerDao.save(answer);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failCount++;
                }
            }
            System.out.println("get answers from user done");
            // 仅当一组数据中 1/3 以上的请求失败才会重新请求整个组
            if (failCount > (BATCH_SIZE / 3)) {
                return ;
            }
            User lastUser = users.get(users.size() - 1);
            progressService.setLastCheckedUserId(lastUser.id);
        }
    }

    @Scheduled(fixedDelay = 1000 * 20)
    public void start() {
        System.out.println("current user count " + userDao.count());
        int lastId = progressService.getLastFollowerId();
//        List<User> users = userDao.getLastN(1);
        List<User> users = userDao.getUsersById(lastId, 1);
        for (User u : users) {
            try {
                System.out.println("get user info " + u.url);
                User newUser = ZhiHuUtil.getUserInfo(u.url);
                if (newUser == null) {
                    userDao.deleteByUrl(u.url);
                    continue;
                }
                System.out.println("user followee " + newUser.followee);
                if (newUser.followee != u.followee) {
                    List<User> followee = ZhiHuUtil.getFollowee(newUser);
                    for (User f : followee) {
                        if (!userDao.isExistUrl(f.url)) {
                            userDao.save(f);
                        }
                    }
                    userDao.update(newUser);
                }
                progressService.setLastFollowerId(u.id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
