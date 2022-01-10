package org.zznode.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.zznode.beans.MD5Utils;
import org.zznode.dto.AuthorizeParam;
import org.zznode.service.RedisService;
import org.zznode.util.GetUuidUtil;
import org.zznode.util.RandomValidateCodeUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/Login")
@Api(tags = "用户登陆接口")
public class
loginController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${upload.showUrl}")
    private String showUrlTmp;


    /**
     * 生成验证码
     */
    @RequestMapping(value = "/getVerifyBase64",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "获取验证码", notes = "获取验证码", httpMethod = "POST",response = JSONObject.class)
    public JSONObject getVerifyBase64( HttpServletResponse response) {
       JSONObject jsonRetu = new JSONObject();
        try {
            //设置相应类型,告诉浏览器输出的内容为图片
            response.setContentType("application/json; charset=utf-8");
            //设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);

            String uuid = new GetUuidUtil().GetUUID();
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            //输出验证码图片方法
            JSONObject json = randomValidateCode.getRandcode();
            //存入reidis后，300秒会失效
            String resule = redisService.setRandom(uuid,json.getString("randomString"));
            jsonRetu.put("uuid",uuid);
            jsonRetu.put("img", "data:image/png;base64," + json.getString("base64"));
            jsonRetu.put("code","000000");
            jsonRetu.put("result","success");
            jsonRetu.put("description","成功");
            System.out.println("resule:" + resule);
            return jsonRetu;
        } catch (Exception e) {
            jsonRetu.put("uuid",null);
            jsonRetu.put("img", null);
            jsonRetu.put("code","000001");
            jsonRetu.put("result","success");
            jsonRetu.put("description","获取验证码失败");
            System.out.println("获取验证码失败"+e);
            return null;
        }
    }

    @RequestMapping(value = "/Authorize",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "登陆获取权限", notes = "登陆获取权限", httpMethod = "POST",response = void.class)
    @ApiImplicitParams({})
    public JSONObject authorize(@Valid @RequestBody AuthorizeParam param) throws NoSuchAlgorithmException {

       String limit =  redisService.getlimit(param.getUserAccount());
       if(limit.equals("lock")){
           JSONObject json = new JSONObject();
           json.put("result","fail");
           json.put("code","000002");
           json.put("description","账号登陆锁定，请稍后再试");
           json.put("authority",null);
           json.put("token",null);
           json.put("userInfo",null);
           return json;
       }
        String randomString = redisService.getRandom(param.getUuid());
        if(randomString == null || randomString.isEmpty() ){
            JSONObject json = new JSONObject();
            json.put("result","fail");
            json.put("code","000003");
            json.put("description","验证码失效");
            json.put("authority",null);
            json.put("token",null);
            json.put("userInfo",null);
            return json;
        }else{
            if(!param.getCheckCode().toUpperCase().equals(randomString)){
                JSONObject json = new JSONObject();
                json.put("result","fail");
                json.put("code","000002");
                json.put("description","验证码错误");
                json.put("Authority",null);
                json.put("Token",null);
                json.put("userInfo",null);
                return json;
            }
        }

        JSONObject jsonUser = redisService.getUser(param.getUserAccount());

        if(jsonUser != null && MD5Utils.getMD5Str(param.getUserPassword()).equals(jsonUser.getString("password"))){

            jsonUser.put("password","******");

            //获取到用户头像时，补上前路径
            String pathtemp = jsonUser.getString("portraitpath");
            if(null == pathtemp){
                jsonUser.put("portraitpath",null);
            }else{
                String portraitpath = showUrlTmp + pathtemp;
                jsonUser.put("portraitpath",portraitpath);
            }

            Map<String, Object> paramIn = new HashMap<String, Object>();

            //获取组织信息
            paramIn.put("partid",jsonUser.getLong("paryid"));
            ResponseEntity<JSONObject> jsonTemp = restTemplate.getForEntity("http://informationLayer/master/serchParyinfo?partid={partid}",JSONObject.class,paramIn);

            JSONObject jsonroles = redisService.getRolesidbyUserid(jsonUser.getLong("userid"));
            System.out.println("jsonroles:" + jsonroles);

            //记录登陆信息
            JSONObject jsonUserOptions = new JSONObject();
            jsonUserOptions.put("userid",jsonUser.getLong("userid"));
            jsonUserOptions.put("account",jsonUser.getString("account"));
            ResponseEntity<JSONObject> jsonuserOption = restTemplate.getForEntity("http://informationLayer/master/insertUserOptions?userid={userid}&account={account}&loginType=1",JSONObject.class,jsonUserOptions);
            System.out.println("jsonuserOption:" + jsonuserOption);

            JSONObject json = new JSONObject();
            String token = new GetUuidUtil().GetUUID();
            System.out.println("token is:" + token);

            //如果用户没有组织，则显示为 上海移动党委
            if(null == jsonTemp || null == jsonTemp.getBody() || null == jsonTemp.getBody().getString("partname")){
                json.put("partname","上海移动党委");
            }else{
                json.put("partname",jsonTemp.getBody().getString("partname"));
            }
            json.put("result","success");
            json.put("code","000000");
            json.put("description","成功");
            json.put("authority",jsonroles);
            json.put("token",token);
            json.put("userInfo",jsonUser);
            //用户的token超时时间是2小时
            redisService.setToken(token,param.getUserAccount());

            Map<String,String> params = new HashMap<>();
            params.put("token",token);
            params.put("auth",jsonroles.toJSONString());
            ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/setAuth?token={token}&auth={auth}",JSONObject.class,params);
            JSONObject body = response.getBody();
            System.out.println("RedisService" + body.get("userAccount"));
            return json;
        }else{
            redisService.setlimit(param.getUserAccount());
            JSONObject json = new JSONObject();
            json.put("result","fail");
            json.put("code","000002");
            json.put("description","用户名或密码错误");
            json.put("authority",null);
            json.put("token",null);
            json.put("userInfo",null);
            return json;
        }
    }

    @RequestMapping(value = "/loginout",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "登出", notes = "登出", httpMethod = "POST",response = void.class)
    @ApiImplicitParams({})
    public void loginout(@RequestHeader("Authorization") String token){
        if(this.checkToken(token)) {
            String userAccount = redisService.getAccount(token);
            JSONObject jsonUser = redisService.getUser(userAccount);
            //记录登出信息
            JSONObject jsonUserOptions = new JSONObject();
            jsonUserOptions.put("userid", jsonUser.getLong("userid"));
            jsonUserOptions.put("account", jsonUser.getString("account"));
            ResponseEntity<JSONObject> jsonuserOption = restTemplate.getForEntity("http://informationLayer/master/insertUserOptions?userid={userid}&account={account}&loginType=2", JSONObject.class, jsonUserOptions);
            System.out.println("jsonuserOption:" + jsonuserOption);

            redisService.deleteRedis(token);
        }
    }

    //================================================utils==================================================//
    //=======================================================================================================//
    private Boolean checkToken(String token){
        Boolean checkType = false;
        String redisToken = redisService.getToken(token);
        if(token.equals(redisToken)) {
            redisService.refreshredis(token);
            checkType = true;
        }
        return checkType;
    }
}
