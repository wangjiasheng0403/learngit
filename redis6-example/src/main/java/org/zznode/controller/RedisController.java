package org.zznode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zznode.beans.User;

import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/RedisUtils")
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/setObject")
    @ResponseBody
    public String setObject(){
        User user = new User(2,"abcd","man1");
        System.out.println("users:" + user.getUserName() );
        ValueOperations<String,User> operations = redisTemplate.opsForValue();
        operations.set("dd",user);
        boolean exists = redisTemplate.hasKey("dd");
        System.out.println("is exists:" + exists);
        User getUser = (User)redisTemplate.opsForValue().get("dd");
        System.out.println(getUser.getUserName() + " : "  + getUser.getUserSex());
        redisTemplate.expire("dd",10, TimeUnit.SECONDS);
        return getUser.getUserName() + " : "  + getUser.getUserSex();

    }

    @RequestMapping("/setString")
    @ResponseBody
    public String setString(){

        ValueOperations<String,String> operations = redisTemplate.opsForValue();
        operations.set("name","shendan");
        boolean exists = redisTemplate.hasKey("name");
        System.out.println("is exists:" + exists);
        String Name = (String)redisTemplate.opsForValue().get("name");
        redisTemplate.expire("name",10, TimeUnit.SECONDS);
        System.out.println("Name is :" + Name);
        return Name;
    }

    @RequestMapping("/delKey")
    @ResponseBody
    public String delKey(){

        ValueOperations<String,String> operations = redisTemplate.opsForValue();
        operations.set("name","shendand");
        boolean exists = redisTemplate.hasKey("name");
        System.out.println("is exists:" + exists);
        String Name = (String)redisTemplate.opsForValue().get("name");
        System.out.println("Name is :" + Name);
        redisTemplate.delete(Name);
        return Name;
    }
}
