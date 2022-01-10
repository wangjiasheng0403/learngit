package org.zznode.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StaticUtils {

//    @Autowired
//    public  RestTemplate restTemplate;

    public static Boolean checkToken(String token){
            Boolean checkType = false;
            String redisToken = getToken(token);
            if(token.equals(redisToken)) {
                refreshredis(token);
                checkType = true;
            }
            return checkType;
    }

    public static String getToken(String token){
        Map<String,String> params = new HashMap<>();
        params.put("token",token);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/getToken?token={token}",JSONObject.class,params);
        JSONObject body = response.getBody();
        if(body.get("token") == null){
            return null;
        }else {
            return body.get("token").toString();
        }
    }

    public static String refreshredis(String token){
        Map<String,String> params = new HashMap<>();
        params.put("token",token);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/refreshredis?token={token}",JSONObject.class,params);
        JSONObject body = response.getBody();
        System.out.println("RedisService" + body.get("result"));
        if(body.get("result") == null){
            return null;
        }else {
            return body.get("result").toString();
        }
    }

    public static List<Long> ConvetDataType(String sourceData)
    {
        return Arrays.stream(sourceData.split(",")).map(s->Long.parseLong(s.trim())).collect(Collectors.toList());
    }
}
