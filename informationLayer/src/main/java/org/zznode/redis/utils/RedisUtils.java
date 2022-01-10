package org.zznode.redis.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    public String setRandom(String uuid, String randomString) {
        System.out.println("RedisUtils==>uuid:" + uuid + " " + "randomString:" + randomString);
        JSONObject json = new JSONObject();
        json.put("uuid", uuid);
        json.put("randomString", randomString);

        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(JSONObject.class));
        redisTemplate.opsForValue().set(json.getString("uuid"), json, 300, TimeUnit.MINUTES);

        boolean exists = redisTemplate.hasKey(uuid);
        if (exists) {
            System.out.println("is exists:" + exists);
            JSONObject jb = (JSONObject) redisTemplate.opsForValue().get(uuid);
            System.out.println("uuid:" + uuid + " " + "random:" + jb.getString("randomString"));
        }
        return "success";
    }

    public String getRandom(String uuid) {
        System.out.println("RedisUtils==>uuid:" + uuid);
        String randomString = null;

        boolean exists = redisTemplate.hasKey(uuid);
        if (exists) {
            System.out.println("is exists:" + exists);
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(JSONObject.class));
            JSONObject js = (JSONObject) redisTemplate.opsForValue().get(uuid);
            randomString = js.getString("randomString");
            System.out.println("uuid:" + uuid + " " + "random:" + randomString);
        } else {
            System.out.println(uuid + "is not exists");
        }
        return randomString;
    }

    public String setToken(String token, String userAccount) {
        System.out.println("RedisUtils==>token:" + token + " | userAccount:" + userAccount);
        JSONObject json = new JSONObject();
        json.put("token", token);
        json.put("userAccount", userAccount);

        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(JSONObject.class));
        redisTemplate.opsForValue().set(json.getString("token"), json, 7200, TimeUnit.MINUTES);

        return "success";
    }

    public JSONObject getToken(String token) {
        System.out.println("RedisUtils==>token:" + token);

        boolean exists = redisTemplate.hasKey(token);
        if (exists) {
            System.out.println("is exists:" + exists);
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(JSONObject.class));
            JSONObject json = (JSONObject) redisTemplate.opsForValue().get(token);
            json.put("result", "success");
            json.put("description", "成功");
            System.out.println("token:" + json.getString("token") + " | " + "userAccount:" + json.getString("userAccount"));
            return json;
        } else {
            System.out.println(token + "is not exists");
            JSONObject jsonRetu = new JSONObject();
            jsonRetu.put("result", "file");
            jsonRetu.put("description", "token is not exists");
            return jsonRetu;
        }
    }

}
