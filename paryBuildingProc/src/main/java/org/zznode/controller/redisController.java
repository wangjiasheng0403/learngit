package org.zznode.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.zznode.redis.utils.RedisUtils;

@RestController
@RequestMapping(value = "/redis")
public class redisController {

    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping("/setRandom")
    @ResponseBody
    public JSONObject setRedis(String uuid,String randomString){
        System.out.println("redisController==>uuid:" + uuid + " " + "randomString:" + randomString );
        String  resule =redisUtils.setRandom(uuid,randomString);
        JSONObject json = new JSONObject();
        json.put("resule",resule);
        return json;
    }

    @RequestMapping("/getRandom")
    @ResponseBody
    public JSONObject getRandom(String uuid){
        System.out.println("redisController==>uuid:" + uuid );
        String  randomString =redisUtils.getRandom(uuid);
        JSONObject json = new JSONObject();
        json.put("random",randomString);
        System.out.println("getRandom" + randomString);
        return json;
    }

    @RequestMapping("/setToken")
    @ResponseBody
    public JSONObject setToken(String token,String userAccount){
        System.out.println("redisController==>token:" + token );
        String  resule =redisUtils.setToken(token,userAccount);
        JSONObject json = new JSONObject();
        json.put("resule",resule);
        System.out.println("setToken" + token);
        return json;
    }

    @RequestMapping("/getToken")
    @ResponseBody
    public JSONObject getToken(String token){
        System.out.println("redisController==>token:" + token );
        JSONObject  jsonRetu =redisUtils.getToken(token);
        System.out.println(jsonRetu.toString());
        return jsonRetu;
    }

    @RequestMapping("/setlimit")
    @ResponseBody
    public JSONObject setlimit(String account){
        System.out.println("redisController==>account:" + account );
        String  resule =redisUtils.setlimit(account);
        JSONObject json = new JSONObject();
        json.put("resule",resule);
        System.out.println("resule" + resule);
        return json;
    }

    @RequestMapping("/getlimit")
    @ResponseBody
    public JSONObject getlimit(String account){
        System.out.println("redisController==>token:" + account );
        String  resule  =redisUtils.getlimit(account);
        JSONObject json = new JSONObject();
        json.put("resule",resule);
        System.out.println("resule" + resule);
        return json;
    }


    @RequestMapping("/setVerificationCode")
    @ResponseBody
    public  JSONObject setVerificationCode(String account,String verificationCode){
        JSONObject json = new JSONObject();
        String result =  redisUtils.setVerificationCode(account,verificationCode);
        json.put("result",result);
        return json;
    }


    @RequestMapping("/getVerificationCode")
    @ResponseBody
    public  JSONObject getVerificationCode(String account){
        JSONObject json = new JSONObject();
        String result =  redisUtils.getVerificationCode(account);
        if(result != null){
            json.put("verificationCode",result);
        }else{
            json.put("verificationCode",null);
        }
        return json;
    }

    @RequestMapping("/refreshredis")
    @ResponseBody
    public  JSONObject refreshredis(String token){
        JSONObject json = new JSONObject();
        redisUtils.refreshRedis(token);
        json.put("result","success");
        return json;
    }

    @RequestMapping("/deleteRedis")
    @ResponseBody
    public  JSONObject deleteRedis(String token){
        JSONObject json = new JSONObject();
        redisUtils.deleteRedis(token);
        json.put("result","success");
        return json;
    }

    @RequestMapping("/setAuth")
    @ResponseBody
    public  JSONObject setAuth(String token,String auth){
        JSONObject json = new JSONObject();
        redisUtils.setAuth(token,auth);
        json.put("result","success");
        return json;
    }

    @RequestMapping("/getAuth")
    @ResponseBody
    public  JSONObject getAuth(String token){
        JSONObject json = redisUtils.getAuth(token);
        return json;
    }
}
