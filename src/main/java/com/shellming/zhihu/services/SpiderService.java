package com.shellming.zhihu.services;

import com.google.gson.Gson;
import com.shellming.zhihu.models.Answer;
import com.shellming.zhihu.models.Comment;
import com.shellming.zhihu.utils.ZhiHuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ruluo1992 on 10/2/2016.
 */
@Service
public class SpiderService {
    private BlockingQueue<Answer> fullAnswersQueue;
    private final int MAX_QUEUE_SIZE = 20;

    @Autowired
    private ThreadPoolExecutor fixedExecutor;

    private int currentPage;
    private final int PAGE_SIZE = 20;
    private final int MAX_TASK = 20;

    private WriteTask writeTask;

    private Gson gson = new Gson();

    class WriteTask implements Runnable {
        private boolean stop = false;
        private BufferedWriter writer;

        public WriteTask() {
            String usrHome = System.getProperty("user.home");
            System.out.println(usrHome);
            try {
                writer = new BufferedWriter(new FileWriter(new File(usrHome, "zhihu.txt"), true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            while (!stop && writer != null) {
                try {
                    Answer answer = fullAnswersQueue.poll(2, TimeUnit.SECONDS);
                    if (answer != null) {
                        writer.append(gson.toJson(answer));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stop() {
            stop = true;
        }
    }

    class FetchTask implements Runnable{
        private Answer answer;
        public FetchTask(Answer answer) {
            this.answer = answer;
        }

        @Override
        public void run() {
//            try {
//                long sleep = 1000 * 60;
//                long MAX_SLEEP = 1000 * 60 * 10;
//                List<Comment> comments = null;
//                while(sleep < MAX_SLEEP) {
//                    try{
//                        Thread.sleep(1000 * 10);
//                        comments = ZhiHuUtil.getComments(answer);
//                        System.out.println("!!!! thread work");
//                        break;
//                    }  catch (IOException e) {
//                        System.out.println("!!!!! thread sleep again");
//                        sleep = sleep * 2;
//                    }
//                }
//                this.answer.comments = comments;
//                fullAnswersQueue.put(this.answer);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

//    @PostConstruct
    public void init() {
        fullAnswersQueue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
        currentPage = 0;
        writeTask = new WriteTask();
        fixedExecutor.execute(writeTask);
    }

//    @Scheduled(fixedDelay = 1000 * 10)
//    public void produce() throws IOException {
//        if (fixedExecutor.getQueue().size() > MAX_TASK) {
//            return ;
//        }
//        List<Answer> answers = ZhiHuUtil.getAnswers(PAGE_SIZE, currentPage * PAGE_SIZE);
//        currentPage++;
//        for (Answer answer : answers) {
//            FetchTask task = new FetchTask(answer);
//            fixedExecutor.execute(task);
//        }
//    }

    @PreDestroy
    public void destroy() {
        writeTask.stop();
    }

}
