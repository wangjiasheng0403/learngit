package org.zznode.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisService {

    @Autowired
    private RestTemplate restTemplate;

    public String setRandom(String uuid,String randomString){
        Map<String,String> params = new HashMap<>();
        params.put("uuid",uuid);
        params.put("randomString",randomString);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/setRandom?uuid={uuid}&randomString={randomString}",JSONObject.class,params);
        JSONObject body = response.getBody();
        System.out.println(body.get("resule"));
        return body.get("resule").toString();
    }

    public String getRandom(String uuid){
        Map<String,String> params = new HashMap<>();
        params.put("uuid",uuid);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/getRandom?uuid={uuid}",JSONObject.class,params);
        JSONObject body = response.getBody();
        System.out.println("RedisService" + uuid + " | " +body.get("random"));
        if(body.get("random") == null){
            return null;
        }else {
            return body.get("random").toString();
        }
    }

    public String getToken(String token){
        Map<String,String> params = new HashMap<>();
        params.put("token",token);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/getToken?token={token}",JSONObject.class,params);
        JSONObject body = response.getBody();
        System.out.println("body:" + body);
        System.out.println("RedisService" + body.get("token"));
        if(body.get("token") == null){
            return null;
        }else {
            return body.get("token").toString();
        }
    }
    //refreshredis
    public String setToken(String token,String userAccount){
        Map<String,String> params = new HashMap<>();
        params.put("token",token);
        params.put("userAccount",userAccount);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/setToken?token={token}&userAccount={userAccount}",JSONObject.class,params);
        JSONObject body = response.getBody();
        System.out.println("RedisService" + body.get("resule"));
        if(body.get("resule") == null){
            return null;
        }else {
            return body.get("resule").toString();
        }
    }

    public String refreshredis(String token){
        Map<String,String> params = new HashMap<>();
        params.put("token",token);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/refreshredis?token={token}",JSONObject.class,params);
        JSONObject body = response.getBody();
        System.out.println("RedisService" + body.get("result"));
        if(body.get("result") == null){
            return null;
        }else {
            return body.get("result").toString();
        }
    }

    public String deleteRedis(String token){
        Map<String,String> params = new HashMap<>();
        params.put("token",token);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/deleteRedis?token={token}",JSONObject.class,params);
        JSONObject body = response.getBody();
        System.out.println("RedisService" + body.get("result"));
        if(body.get("result") == null){
            return null;
        }else {
            return body.get("result").toString();
        }
    }

    public String getlimit(String account){
        Map<String,String> params = new HashMap<>();
        params.put("account",account);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/getlimit?account={account}",JSONObject.class,params);
        JSONObject body = response.getBody();
        System.out.println("body:" + body);
        System.out.println("RedisService" + body.get("resule"));
        if(body.get("resule") == null){
            return null;
        }else {
            return body.get("resule").toString();
        }
    }

    public String setlimit(String account){
        Map<String,String> params = new HashMap<>();
        params.put("account",account);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/setlimit?account={account}",JSONObject.class,params);
        JSONObject body = response.getBody();
        System.out.println("RedisService" + body.get("resule"));
        if(body.get("resule") == null){
            return null;
        }else {
            return body.get("resule").toString();
        }
    }



    public String getAccount(String token){
        Map<String,String> params = new HashMap<>();
        params.put("token",token);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/getToken?token={token}",JSONObject.class,params);
        JSONObject body = response.getBody();
        System.out.println("RedisService" + body.get("userAccount"));
        if(body.get("userAccount") == null){
            return null;
        }else {
            return body.get("userAccount").toString();
        }
    }

    public JSONObject getUser(String account){
        Map<String,String> params = new HashMap<>();
        System.out.println("account:" + account);
        params.put("account",account);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://informationLayer/master/serchUserinfoByAcount?account={account}",JSONObject.class,params);
        JSONObject body = response.getBody();
        if(null == body){
            System.out.println("没有这个用户");
            return null;
        }
        System.out.println("RedisService" + body.toString());
        return body;
    }

    public JSONObject getRolesidbyUserid(Long userid){
        Map<String,Long> params = new HashMap<>();
        System.out.println("userid:" + userid);
        params.put("userid",userid);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://informationLayer/master/serchRolesIDByUserID?userid={userid}",JSONObject.class,params);
        JSONObject body = response.getBody();

        System.out.println("Rolesinfo" + body.toString());
        return body;
    }
}
