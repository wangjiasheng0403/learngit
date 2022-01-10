//package org.zznode.task;
//
//
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class ScheduledExecutorDemo {
//
//    private ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(5);
//
//    @PostConstruct
//    public void demoTask() {
//        executor.scheduleAtFixedRate(new Runnable() {
//
//            @Override
//            public void run() {
//                System.out.println("[ ScheduledExecutorDemo ]\t" + System.currentTimeMillis());
//            }
//        }, 0, 5, TimeUnit.SECONDS);
//
//    }
//
//    @PreDestroy
//    public void destory() {
//        executor.shutdown();
//    }
//
//}
