package org.zznode.task;


import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zznode.controller.UsersController;
import org.zznode.dao.TbRoleLogsMapper;
import org.zznode.dao.TbUsersOptionsMapper;
import org.zznode.dao.TbUsersScoreMapper;
import org.zznode.entity.TbUsers;
import org.zznode.entity.TbUsersScore;
import org.zznode.service.impl.UsersServiceImpl;
import org.zznode.util.SnowflakeIdGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledDemo {

    @Autowired
    private TbUsersOptionsMapper tbUsersOptionsMapper;

    @Autowired
    private TbRoleLogsMapper tbRoleLogsMapper;

    @Autowired
    private UsersServiceImpl userService;

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Autowired
    private TbUsersScoreMapper tbUsersScoreMapper;
//    serchuserscore

    @Scheduled(cron = "${cron.updatescore}")
    public void demoTask() {
        JSONObject jsonObject = new JSONObject();

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("YYYY");
        String dateYear = format.format(date);
        format = new SimpleDateFormat("YYYY-MM");
        String datemonth = format.format(date);
        String total= "total";

        System.out.println("dateYear:" + dateYear +  "datemonth:" + datemonth);

       List<Long> useridlist = tbUsersOptionsMapper.serchUseridList();

       for(Long s:useridlist) {

           System.out.println(s);
           jsonObject =  this.serchscore(s);
           TbUsers tbUsers = userService.selectById(s);
           System.out.println(jsonObject);
           System.out.println(tbUsers);

           TbUsersScore serchuserscore = tbUsersScoreMapper.serchuserscore(s, datemonth);
           if(null != serchuserscore){
               serchuserscore.setScore(serchuserscore.getScore() + jsonObject.getInteger("dayscore"));
               tbUsersScoreMapper.updateByPrimaryKey(serchuserscore);
           }else{
               TbUsersScore t = new TbUsersScore();
               t.setId(snowflakeIdGenerator.nextId());
               t.setUserid(s);
               t.setUsername(tbUsers.getUsername());
               t.setTimequantum(datemonth);
               t.setScore(jsonObject.getInteger("dayscore"));
               t.setParyid(tbUsers.getParyid());
               t.setCreatetime(date);
               tbUsersScoreMapper.insertOne(t);
           }

           serchuserscore = tbUsersScoreMapper.serchuserscore(s, dateYear);
           if(null != serchuserscore){
               serchuserscore.setScore(serchuserscore.getScore() + jsonObject.getInteger("dayscore"));
               tbUsersScoreMapper.updateByPrimaryKey(serchuserscore);
           }else{
               TbUsersScore t = new TbUsersScore();
               t.setId(snowflakeIdGenerator.nextId());
               t.setUserid(s);
               t.setUsername(tbUsers.getUsername());
               t.setTimequantum(dateYear);
               t.setScore(jsonObject.getInteger("dayscore"));
               t.setParyid(tbUsers.getParyid());
               t.setCreatetime(date);
               tbUsersScoreMapper.insertOne(t);
           }

           serchuserscore = tbUsersScoreMapper.serchuserscore(s, total);
           if(null != serchuserscore){
               serchuserscore.setScore(serchuserscore.getScore() + jsonObject.getInteger("dayscore"));
               tbUsersScoreMapper.updateByPrimaryKey(serchuserscore);
           }else{
               TbUsersScore t = new TbUsersScore();
               t.setId(snowflakeIdGenerator.nextId());
               t.setUserid(s);
               t.setUsername(tbUsers.getUsername());
               t.setTimequantum(total);
               t.setScore(jsonObject.getInteger("dayscore"));
               t.setParyid(tbUsers.getParyid());
               t.setCreatetime(date);
               tbUsersScoreMapper.insertOne(t);
           }

       }

        System.out.println("[ ScheduledDemo ]\t\t" + System.currentTimeMillis());
    }

    public JSONObject serchscore(Long userid){

        int lc =  tbRoleLogsMapper.selectLogincountByyesterday(userid);
        int wc = tbRoleLogsMapper.selectWatchcountByyesterday(userid);
        int mc =  tbRoleLogsMapper.selectMakecountByyesterday(userid);

        System.out.println("lc:" + lc + "wc:" + wc + "mc:" + mc);

        int lcscore=0;
        int wcscore=0;
        int mcscore=0;

        JSONObject json = new JSONObject();

        if(lc > 0) {
            lcscore = 2;
        }

        if(wc > 0 && wc < 4){
            wcscore = wc *2;
        }else if( wc > 3){
            wcscore = 6;
        }

        if(mc > 0 && mc < 3){
            mcscore = mc *1;
        }else if(mc > 2){
            mcscore = 2;
        }

        json.put("userid",userid );
        json.put("loginscore",lcscore );
        json.put("watchscore",wcscore);
        json.put("mcscore",mcscore);
        json.put("dayscore",lcscore + wcscore + mcscore);

        return json;
    }

}
