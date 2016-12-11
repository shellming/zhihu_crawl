package com.shellming.zhihu.services;

import com.shellming.zhihu.dao.AnswerDao;
import com.shellming.zhihu.models.Answer;
import com.shellming.zhihu.models.Comment;
import com.shellming.zhihu.utils.ZhiHuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruluo1992 on 11/2/2016.
 */
@Service
@Profile("!test")
public class DuanziSelectService {
    private static final String[] keywords = new String[]{
            "哈哈", "233", "段子", "可爱", "666", "机智", "红红火火", "好笑",
            "好玩", "观光团", "丧心病狂", "有毒", "笑话", "膝盖", "节操",
            "滑稽", "欢乐", "幽默", "太逗", "内涵", "笑喷", "hhh", "haha",
            "笑死", "抖机灵"
    };
    private final String URL_TEMPLATE = "https://www.zhihu.com/question/%s/answer/%s";

    private List<String> duanziUrls;
    private final Integer MAX_SIZE = 100;
    private final Integer BATCH_SIZE = 1;
    private final Integer THRESHOLD = 3;
    private volatile Boolean stop = false;

    @Autowired
    private ProgressService progressService;

    @Autowired
    private AnswerDao answerDao;

    @PostConstruct
    private void init() {
        duanziUrls = new ArrayList<>(MAX_SIZE);
    }

    @Scheduled(fixedDelay = 1000)
    private void start() throws Exception {
        if (!stop) {
            int id = progressService.getLastCheckedDuanzi();
            List<Answer> answers = answerDao.getAnswersById(id, BATCH_SIZE);
            for (int i = 0; i < answers.size() && !stop; i++) {
                Answer answer = answers.get(i);
                if (isDuanzi(answer)) {
                    String url = String.format(URL_TEMPLATE, answer.questionUrl, answer.token);
                    if (!addDuanzi(url)) {
                        stop = true;
                    }
                }
                if (!stop) {
                    progressService.setLastCheckedDuanzi(answer.id);
                }
            }
        }
    }

    public synchronized boolean addDuanzi(String url) {
        if (duanziUrls.size() >= MAX_SIZE) {
            return false;
        }
        duanziUrls.add(url);
        return true;
    }

    public synchronized List<String> getDuanziUrls() {
        List<String> copy = new ArrayList<>(duanziUrls);
        return copy;
    }

    public synchronized void clear() {
        duanziUrls.clear();
    }

    private boolean isDuanzi(Answer answer) throws Exception {
        List<Comment> comments = ZhiHuUtil.getComments(answer);
        int count = 0;
        for (Comment comment : comments) {
            for (String keyword : keywords) {
                if (comment.content.contains(keyword)) {
                    count++;
                    break;
                }
            }
        }
        return count > THRESHOLD;
    }

}
