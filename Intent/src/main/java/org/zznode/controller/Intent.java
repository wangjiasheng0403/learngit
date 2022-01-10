package org.zznode.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.zznode.dto.AuthorizeDataWithDepartIDParam;
import org.zznode.dto.AuthorizeOpenDataParam;
import org.zznode.service.RedisService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = "/Intent")
@Api(tags = "展示门户相关接口")
public class Intent {

    @Value("${upload.saveUrl}")
    private String saveUrl;

    @Value("${upload.showUrl}")
    private String showUrlTmp;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RestTemplate restTemplate;


    @RequestMapping(value = "AuthorizeDate",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "党组织地图数据权限接口", notes = "通过token获取到党组织地图的组织树及登陆用户所在部门的场景列表", httpMethod = "POST",response = void.class)
    @ApiImplicitParams({})
    public JSONObject authorizeDate(@RequestHeader("Authorization") String token) {

        String userAccount = redisService.getAccount(token);
        JSONObject jsonUser = redisService.getUser(userAccount);

        List<String> nulllist = new ArrayList<String>();

        if (this.checkToken(token)) {

            //取部门组织树
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userid",jsonUser.getLong("userid"));
            //0是和创树
            param.put("relationType",0);
            ResponseEntity<JSONObject> jsonDepartLV1 = restTemplate.getForEntity("http://informationLayer/master/viewDepartTree?userid={userid}&relationType={relationType}",JSONObject.class,param);

            //取组织场景
            Long paryid = jsonUser.getLong("paryid");
            Map<String, Long> m = new HashMap<>();
            m.put("paryid", paryid);
            m.put("userid", jsonUser.getLong("userid"));
            ResponseEntity<JSONArray> jsonMateArray = restTemplate.getForEntity("http://informationLayer/recommend/serchPartydSceneData?paryid={paryid}&userid={userid}", JSONArray.class, m);

            JSONObject jsonAuthorizeDate = new JSONObject();
            jsonAuthorizeDate.put("result", "success");
            jsonAuthorizeDate.put("code", "000000");
            jsonAuthorizeDate.put("description", "成功");
            jsonAuthorizeDate.put("mateList", jsonMateArray.getBody());
            jsonAuthorizeDate.put("administrativeTree",jsonDepartLV1.getBody().get("administrativeTree"));

            return jsonAuthorizeDate;
        } else {
            JSONObject json = new JSONObject();
            json.put("result", "fail");
            json.put("code", "000001");
            json.put("description", "token不存在或已过期，请登陆");
            json.put("mateList", nulllist);
            json.put("administrativeTree", nulllist);
            return json;
        }
    }


    @RequestMapping(value = "AuthorizeDataWithDepartID",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "通过组织ID查询组织场景接口", notes = "通过paryid查询在dataresource表中dataBelong字段和partid一致的资源数据", httpMethod = "POST",response = void.class)
    @ApiImplicitParams({})
    public JSONObject authorizeDataWithDepartID(@RequestHeader("Authorization") String token, @Valid @RequestBody AuthorizeDataWithDepartIDParam param)  {

        //如果是null，返回一个空数组
        List<String> nullList =new ArrayList<>();

        String userAccount = redisService.getAccount(token);
        JSONObject jsonUser = redisService.getUser(userAccount);

        if(this.checkToken(token)){
            Map<String,Long> m =new HashMap<>();
            m.put("paryid",param.getDepartID());
            m.put("userid",jsonUser.getLong("userid"));
            ResponseEntity<JSONArray> jsonMateArray = restTemplate.getForEntity("http://informationLayer/recommend/serchPartydSceneData?paryid={paryid}&userid={userid}",JSONArray.class,m);

            if(null == param.getPageIndex() || null ==  param.getPageSize() || param.getPageIndex() < 1 || param.getPageSize() <1){
                JSONObject jsonAuthorizeDate = new JSONObject();
                jsonAuthorizeDate.put("result","success");
                jsonAuthorizeDate.put("code","000000");
                jsonAuthorizeDate.put("description","成功");
                jsonAuthorizeDate.put("mateList",jsonMateArray.getBody());
                jsonAuthorizeDate.put("total",jsonMateArray.getBody().size());
                return jsonAuthorizeDate;
            }

            int pageBegin = 0;
            int pageEnd = 0;

            pageBegin =(param.getPageIndex() - 1) * param.getPageSize();
            pageEnd = param.getPageIndex() * param.getPageSize();

            JSONArray retJsonarray = new JSONArray();
            if(pageBegin >= jsonMateArray.getBody().size()){
                JSONObject json = new JSONObject();
                json.put("result","success");
                json.put("code","000000");
                json.put("description","成功");
                json.put("total",jsonMateArray.getBody().size());
                json.put("mateList",nullList);
                return json;
            }

            for(int i=pageBegin; i<pageEnd&&i<jsonMateArray.getBody().size(); i++){
                retJsonarray.add(jsonMateArray.getBody().get(i));
            }

            JSONObject json = new JSONObject();
            json.put("result","success");
            json.put("code","000000");
            json.put("description","成功");
            json.put("total",jsonMateArray.getBody().size());
            json.put("mateList",retJsonarray);

            return json;
        }else{
            JSONObject json = new JSONObject();
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
            json.put("mateList",nullList);
            json.put("total",nullList);
            return json;
        }
    }

    @RequestMapping(value = "AuthorizeOpenData",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "案例资源库数据接口", notes = "查询资源表中，datatype=2,showtype=1的资源列表", httpMethod = "POST",response = void.class)
    @ApiImplicitParams({})
    public JSONObject authorizeOpenData(@RequestHeader(value ="Authorization",required = false) String token,@Valid @RequestBody AuthorizeOpenDataParam param){

        List<String> nulllist = new ArrayList<String>();
        Long userid =0l;

        if(null != token) {
            String userAccount = redisService.getAccount(token);
            if (null != userAccount) {
                JSONObject jsonUser = redisService.getUser(userAccount);
                userid = jsonUser.getLong("userid");
            }
        }

        Map<String,Long> m =new HashMap<>();
        m.put("userid",userid);
        ResponseEntity<JSONArray> response = restTemplate.getForEntity("http://informationLayer/recommend/serchOpenData?userid={userid}",JSONArray.class,m);
        int pageBegin = 0;
        int pageEnd = 0;

        pageBegin =(param.getPageIndex() - 1) * param.getPageSize();
        pageEnd = param.getPageIndex() * param.getPageSize();

        JSONArray retJsonarray = new JSONArray();

        if(pageBegin >= response.getBody().size()){
            JSONObject json = new JSONObject();
            json.put("result","success");
            json.put("code","000000");
            json.put("description","成功");
            json.put("mateList",nulllist);
            json.put("total",response.getBody().size());
            return json;
        }

        for(int i=pageBegin; i<pageEnd&&i<response.getBody().size(); i++){
            retJsonarray.add(response.getBody().get(i));
        }

        JSONObject json = new JSONObject();
        json.put("result","success");
        json.put("code","000000");
        json.put("description","成功");
        json.put("mateList",retJsonarray);
        json.put("total",response.getBody().size());
        return json;
    }

    @RequestMapping(value = "RecommendData",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "推荐模版数据接口", notes = "从资源表中取apriveOrder不为空的，取10条，从小到大排序返回", httpMethod = "POST",response = void.class)
    @ApiImplicitParams({})
    public JSONObject recommendData(@RequestHeader(value ="Authorization",required = false) String token){

        Long userid =0l;

        if(null != token) {
            String userAccount = redisService.getAccount(token);
            if (null != userAccount) {
                JSONObject jsonUser = redisService.getUser(userAccount);
                userid = jsonUser.getLong("userid");
            }
        }

        Map<String,Long> m =new HashMap<>();
        m.put("userid",userid);
        ResponseEntity<JSONArray> response = restTemplate.getForEntity("http://informationLayer/recommend/serchRecommendData?userid={userid}",JSONArray.class,m);

        JSONObject json = new JSONObject();
        json.put("result","success");
        json.put("code","000000");
        json.put("description","成功");
        json.put("mateList",response.getBody());

        return json;
    }

    @RequestMapping(value = "RedData",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "红色基地数据接口", notes = "取资源表中datatype=1的数据列表", httpMethod = "POST",response = void.class)
    @ApiImplicitParams({})
    public JSONObject redData(@RequestHeader(value ="Authorization",required = false) String token) {

        Long userid =0l;

        if(null != token) {
            String userAccount = redisService.getAccount(token);
            if (null != userAccount) {
                JSONObject jsonUser = redisService.getUser(userAccount);
                userid = jsonUser.getLong("userid");
            }
        }

        Map<String,Long> m =new HashMap<>();
        m.put("userid",userid);
        ResponseEntity<JSONArray> response = restTemplate.getForEntity("http://informationLayer/recommend/serchRedData?userid={userid}",JSONArray.class,m);

        JSONObject json = new JSONObject();
        json.put("result","success");
        json.put("code","000000");
        json.put("description","成功");
        json.put("mateList",response.getBody());

        return json;
    }

    @RequestMapping(value = "AuthorizeJoinDate",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "支部共建圈数据接口", notes = "支部共建圈数据权限接口", httpMethod = "POST",response = void.class)
    @ApiImplicitParams({})
    public JSONObject AuthorizeJoinDate(@RequestHeader("Authorization") String token,@Valid @RequestBody AuthorizeOpenDataParam param){

        List<String> nulllist = new ArrayList<String>();

        String userAccount = redisService.getAccount(token);
        JSONObject jsonUser = redisService.getUser(userAccount);

        if(this.checkToken(token)){

            //获取组织树
            Map<String, Object> paramIn = new HashMap<String, Object>();
            paramIn.put("userid",jsonUser.getLong("userid"));
            //1是共建树
            paramIn.put("relationType",1);
            ResponseEntity<JSONObject> jsonDepartLV1 = restTemplate.getForEntity("http://informationLayer/master/viewDepartTree?userid={userid}&relationType={relationType}",JSONObject.class,paramIn);

            //获取部门场景
            Long paryid = jsonUser.getLong("paryid");
            System.out.println("paryid:" + paryid);
            Map<String,Long> m =new HashMap<>();
            m.put("paryid",paryid);
            m.put("userid", jsonUser.getLong("userid"));
            ResponseEntity<JSONArray> jsonMateArray = restTemplate.getForEntity("http://informationLayer/recommend/serchPartydSceneData?paryid={paryid}&userid={userid}",JSONArray.class,m);

            int pageBegin =(param.getPageIndex() - 1) * param.getPageSize();
            int pageEnd = param.getPageIndex() * param.getPageSize();

            JSONArray retJsonarray = new JSONArray();

            if(pageBegin >= jsonMateArray.getBody().size()){
                JSONObject json = new JSONObject();
                json.put("result","success");
                json.put("code","000000");
                json.put("description","成功");
                json.put("mateList",nulllist);
                json.put("administrativeTree",nulllist);
                json.put("total",jsonMateArray.getBody().size());
                return json;
            }

            for(int i=pageBegin; i<pageEnd&&i<jsonMateArray.getBody().size(); i++){
                retJsonarray.add(jsonMateArray.getBody().get(i));
            }

            JSONObject jsonAuthorizeDate = new JSONObject();
            jsonAuthorizeDate.put("result","success");
            jsonAuthorizeDate.put("code","000000");
            jsonAuthorizeDate.put("description","成功");
            jsonAuthorizeDate.put("mateList",retJsonarray);
            jsonAuthorizeDate.put("administrativeTree",jsonDepartLV1.getBody().get("administrativeTree"));
            jsonAuthorizeDate.put("total",jsonMateArray.getBody().size());

            return jsonAuthorizeDate;
        }else{
            JSONObject json = new JSONObject();
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
            json.put("mateList",nulllist);
            json.put("administrativeTree",nulllist);
            json.put("total",0);
            return json;
        }
    }

    @RequestMapping(value = "/uploadPortrait",method = RequestMethod.POST)
    @ApiOperation(value = "用户自定义头像上传功能", notes = "用户自定义头像上传功能，上传后记录在头像路径记录在用户表中，复用了nginx83端口。", httpMethod = "POST",response = void.class)
    @ApiImplicitParams({})
    public JSONObject addPortrait(@RequestHeader("Authorization") String token,@RequestParam("photos") MultipartFile file, HttpServletRequest request) throws IOException {
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {

            String path = null;
            String rpath = null;
            double fileSize = file.getSize();
            String userAccount = redisService.getAccount(token);
            JSONObject jsonUser = redisService.getUser(userAccount);
            System.out.println("userid:" + jsonUser.getLong("userid"));
            if (file != null) {
                String type = null;
                String fileName = file.getOriginalFilename();
                System.out.println("filename:" + fileName);

                type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
                if (type != null) {
                    if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase())) {
                        String realPath = request.getSession().getServletContext().getRealPath("/");

                        String trueFileName = String.valueOf(System.currentTimeMillis()) + "." + type;

                        path = "/portrait/" + trueFileName;
                        rpath = saveUrl + path;
                        System.out.println("filepath:" + rpath);

                        file.transferTo(new File(rpath));

                        Map<String, Object> params = new HashMap<>();
                        System.out.println("usersid:" + jsonUser.getLong("usersid") + "portraitpath:" + path);
                        params.put("portraitpath", path);
                        params.put("usersid", jsonUser.getLong("userid"));
                        System.out.println("123=====>" + jsonUser.getLong("userid"));
                        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://informationLayer/master/updateUserinfoByusersid?usersid={usersid}&portraitpath={portraitpath}", JSONObject.class, params);
                        JSONObject body = response.getBody();
                        json.put("portraitUrl",showUrlTmp + path);
                        System.out.println("body:" + body);
                    }
                } else {
                    json.put("description", "文件格式不合法");
                    json.put("result", "fail");
                    json.put("code", "000002");
                    return json;
                }
            }

            json.put("description", "上传成功");
            json.put("result", "success");
            json.put("code", "000000");
            return json;
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
            return json;
        }
    }

    //================================================给睿悦的接口=============================================//
    //=======================================================================================================//

    @RequestMapping(value = "getMakes",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "查询某个资源的所有场景评论内容", notes = "通过dataid查找role_logs获取访问列表，通过访问列表到makes获取访问明细", httpMethod = "POST",response = void.class)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "场景ID", name = "dataid", dataType = "String", required = true,paramType = "query")})
    public JSONObject getMakes(String dataid){

        Map<String,String> params = new HashMap<>();
        params.put("dataid",dataid);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://informationLayer/master/serchMakesBydataID?dataid={dataid}",JSONObject.class,params);
        JSONObject body = response.getBody();
        return body;
    }

    @RequestMapping(value = "getCollect",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "查询场景评论内容", notes = "根据资源ID和用户ID查询改用户对该场景的所有评论", httpMethod = "POST",response = void.class)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "场景ID", name = "dataid", dataType = "String", required = true,paramType = "query"),
            @ApiImplicitParam(value = "用户ID", name = "userid", dataType = "String", required = true,paramType = "query")})
    public JSONObject getCollect(String dataid,String userid){

        Map<String,Object> params = new HashMap<>();

        params.put("dataid",dataid);
        params.put("userid",userid);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://informationLayer/master/serchCollectBydataIDAndUserid?dataid={dataid}&userid={userid}",JSONObject.class,params);
        JSONObject body = response.getBody();

        return body;
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
