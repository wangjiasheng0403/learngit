//package org.zznode.task;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class TimerDemo {
//
//    private Timer timer = new Timer(true);
//
//    @PostConstruct
//    public void demoTask() {
//        timer.scheduleAtFixedRate(new TimerTask() {
//
//            @Override
//            public void run() {
//                System.out.println("[ TimerDemo ]\t\t\t" + System.currentTimeMillis());
//            }
//        }, 0, 5000);
//
//    }
//
//    @PreDestroy
//    public void destory() {
//        timer.cancel();
//    }
//
//}
