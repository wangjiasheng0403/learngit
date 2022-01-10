package org.zznode.redis.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.TimeUnit;

@Service
public class RedisUtils {

    @Value("${login.limit}")
    private int loginlimit;

    @Autowired
    private RedisTemplate redisTemplate;

    public String setRandom(String uuid,String randomString){
        System.out.println("RedisUtils==>uuid:" + uuid + " " + "randomString:" + randomString );
        JSONObject json = new JSONObject();
        json.put("uuid",uuid);
        json.put("randomString",randomString);

        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(JSONObject.class));
        redisTemplate.opsForValue().set(json.getString("uuid"),json,300,TimeUnit.SECONDS);

        boolean exists = redisTemplate.hasKey(uuid);
        if( exists ) {
            System.out.println("is exists:" + exists);
            JSONObject jb= (JSONObject) redisTemplate.opsForValue().get(uuid);
            System.out.println("uuid:" + uuid + " " + "random:" + jb.getString("randomString"));
        }
        return "success";
    }

    public String setVerificationCode(String account,String verificationCode){
        System.out.println("RedisUtils==>account:" + account + " " + "verificationCode:" + verificationCode );
        JSONObject json = new JSONObject();
        json.put("account","Verification" + account);
        json.put("verificationCode",verificationCode);

        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(JSONObject.class));
        redisTemplate.opsForValue().set(json.getString("account"),json,300,TimeUnit.SECONDS);

        boolean exists = redisTemplate.hasKey(json.getString("account"));
        if( exists ) {
            System.out.println("is exists:" + exists);
            JSONObject jb= (JSONObject) redisTemplate.opsForValue().get(json.getString("account"));
            System.out.println("account:" + jb.getString("account") + " " + "verificationCode:" + jb.getString("verificationCode"));
        }
        return "success";
    }

    public String getVerificationCode(String account){
        System.out.println("RedisUtils==>account:" + account );

        String accountKey = "Verification" + account;
        boolean exists = redisTemplate.hasKey(accountKey);
        if( exists ) {
            System.out.println("is exists:" + exists);
            JSONObject jb= (JSONObject) redisTemplate.opsForValue().get(accountKey);
            System.out.println("RedisUtils account:" + jb.getString("account") + " " + "verificationCode:" + jb.getString("verificationCode"));
            return jb.getString("verificationCode");
        }
       return null;
    }

    public String getRandom(String uuid){
        System.out.println("RedisUtils==>uuid:" + uuid);
        String randomString = null;

        boolean exists = redisTemplate.hasKey(uuid);
        if( exists ) {
            System.out.println("is exists:" + exists);
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(JSONObject.class));
            JSONObject js =  (JSONObject)redisTemplate.opsForValue().get(uuid);
            randomString = js.getString("randomString");
            System.out.println("uuid:" + uuid + " " + "random:" + randomString);
        }else{
            System.out.println(uuid + "is not exists");
        }
        return randomString;
    }

    public String setToken(String token,String userAccount){
        System.out.println("RedisUtils==>token:" + token + " | userAccount:" + userAccount);
        JSONObject json = new JSONObject();
        json.put("token",token);
        json.put("userAccount",userAccount);

        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(JSONObject.class));
        redisTemplate.opsForValue().set(json.getString("token"),json,7200,TimeUnit.SECONDS);

        return "success";
    }

    public JSONObject getToken(String token){
        System.out.println("RedisUtils==>token:" + token);

        boolean exists = redisTemplate.hasKey(token);
        if( exists ) {
            System.out.println("is exists:" + exists);
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(JSONObject.class));
            JSONObject json =  (JSONObject)redisTemplate.opsForValue().get(token);
            json.put("result","success");
            json.put("description","成功");
            System.out.println("token:" + json.getString("token") + " | " + "userAccount:" + json.getString("userAccount"));
            return json;
        }else{
            System.out.println(token + "is not exists");
            JSONObject jsonRetu = new JSONObject();
            jsonRetu.put("result","file");
            jsonRetu.put("description","token is not exists");
            return jsonRetu;
        }
    }


    public String setlimit(String account){
        System.out.println("RedisUtils==>account:" + account );

        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(JSONObject.class));

        boolean exists = redisTemplate.hasKey("LOGINLOCK" +account);
        if( exists ) {
            JSONObject json =  (JSONObject)redisTemplate.opsForValue().get("LOGINLOCK" +account);
            int limit  = json.getInteger("limit");
            json.put("limit",limit+1);
            redisTemplate.opsForValue().set(json.getString("token"),json,300,TimeUnit.SECONDS);
        }else{
            JSONObject json = new JSONObject();
            json.put("token","LOGINLOCK" +account);
            json.put("limit",1);

            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(JSONObject.class));
            redisTemplate.opsForValue().set(json.getString("token"),json,300,TimeUnit.SECONDS);
        }
        return "success";
    }

    public String getlimit(String account){
        System.out.println("RedisUtils==>account:" + account);

        boolean exists = redisTemplate.hasKey("LOGINLOCK" +account);
        if( exists ) {
            System.out.println("is exists:" + exists);
            JSONObject json =  (JSONObject)redisTemplate.opsForValue().get("LOGINLOCK" +account);
            int limit  = json.getInteger("limit");
            if(limit > loginlimit ){
                return "lock";
            }
        }
        return "success";
    }


    public String setAuth(String token,String auth){
        System.out.println("RedisUtils==>token:" + token + " | auth:" + auth);
        JSONObject json = new JSONObject();
        json.put("token", "AUTH" +token);
        json.put("auth",auth);

        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(JSONObject.class));
        redisTemplate.opsForValue().set(json.getString("token"),json,7200,TimeUnit.SECONDS);

        return "success";
    }

    public JSONObject getAuth(String token){
        System.out.println("RedisUtils==>token:" + token);
        JSONObject json = new JSONObject();
        boolean exists = redisTemplate.hasKey("AUTH" + token);
        if( exists ) {
            System.out.println("is exists:" + exists);
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(JSONObject.class));
            json =  (JSONObject)redisTemplate.opsForValue().get("AUTH" + token);
            System.out.println("token:" + json.getString("token") + " | " + "auth:" + json.getString("auth"));
        }else{
            System.out.println(token + "is not exists");
            json.put("result","file");
            json.put("description","token is not exists");
        }
        return json;
    }



    public void refreshRedis(String token){
        redisTemplate.expire(token,7200,TimeUnit.SECONDS);
    }

    public void deleteRedis(String token){
        redisTemplate.delete(token);
    }
}
