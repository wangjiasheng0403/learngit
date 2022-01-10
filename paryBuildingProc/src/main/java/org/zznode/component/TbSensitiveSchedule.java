package org.zznode.component;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author bh
 */
@Component
public class TbSensitiveSchedule {


    @Scheduled(cron = " 0 0 0 0 ?")
    private void checkTbSensitive(){

    }
}
