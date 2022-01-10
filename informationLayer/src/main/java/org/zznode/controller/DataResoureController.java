package org.zznode.controller;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.zznode.dto.InsertDataResourceInfoParam;
import org.zznode.dto.SerchDataResourceByNameParam;
import org.zznode.service.impl.TbDataResourceServiceImpl;
import org.zznode.util.StaticUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RequestMapping(value = "/datasource")
@RestController
@Api(tags = "三个场景管理接口")
public class DataResoureController {

    @Autowired
    private TbDataResourceServiceImpl tbDataResourceService;

    @Autowired
    private RestTemplate restTemplate;


    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "serchDataResourceByName",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "根据资源名字模糊查询信息接口", notes = "根据资源名字模糊查询信息接口", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject serchDataResourceByName(@RequestHeader("Authorization") String token,@Valid @RequestBody SerchDataResourceByNameParam param){
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            List<String> nulllist = new ArrayList<String>();

            JSONArray jsonArray = tbDataResourceService.serchdataresource(param);
            if (jsonArray.size() == 0) {
                json.put("datarecourcelist", null);
                json.put("result", "falt");
                json.put("code", "000002");
                json.put("description", "没有符合条件的数据");
                return json;
            }

            int pageBegin = (param.getPageIndex() - 1) * param.getPageSize();
            int pageEnd = param.getPageIndex() * param.getPageSize();
            JSONArray retJsonarray = new JSONArray();

            if (pageBegin >= jsonArray.size()) {
                json.put("result", "success");
                json.put("code", "000000");
                json.put("description", "成功");
                json.put("datarecourcelist", nulllist);
                json.put("total", jsonArray.size());
                return json;
            }

            for (int i = pageBegin; i < pageEnd && i < jsonArray.size(); i++) {
                retJsonarray.add(jsonArray.get(i));
            }
            json.put("result", "success");
            json.put("code", "000000");
            json.put("description", "成功");
            json.put("datarecourcelist", retJsonarray);
            json.put("total", jsonArray.size());
        }else{
            json.put("description","token无效");
            json.put("code","000001");
            json.put("result","falt");
        }
        return json;
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "insertDataResourceInfo",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "添加资源信息", notes = "添加资源信息", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject insertDataResourceInfo(@RequestHeader("Authorization") String token,@Valid @RequestBody InsertDataResourceInfoParam param){

        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            json = tbDataResourceService.insertTbDataResource(param);
        }else{
            json.put("description","token无效");
            json.put("code","000001");
            json.put("result","falt");
        }
       return json;
    }


    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "updateDataResourceInfo",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "修改资源信息", notes = "修改资源信息", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject updateDataResourceInfo(@RequestHeader("Authorization") String token,@Valid @RequestBody InsertDataResourceInfoParam param){

        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            json = tbDataResourceService.updateDataResourceInfo(param);
        }else{
            json.put("description","token无效");
            json.put("code","000001");
            json.put("result","falt");
        }
        return json;
    }



    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "deleteDataResourceinfo",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "删除资源", notes = "删除资源", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject deleteUserinfo(@Valid @RequestBody InsertDataResourceInfoParam param,@RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            json = tbDataResourceService.deleteDataResource(param.getDataid());
        }else{
            json.put("description","token无效");
            json.put("code","000001");
            json.put("result","falt");
        }
        return json;

    }


    public Boolean checkToken(String token){
        Boolean checkType = false;
        String redisToken = getToken(token);
        if(token.equals(redisToken)) {
            refreshredis(token);
            checkType = true;
        }
        return checkType;
    }

    public String getToken(String token){
        Map<String,String> params = new HashMap<>();
        params.put("token",token);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/getToken?token={token}",JSONObject.class,params);
        JSONObject body = response.getBody();
        if(body.get("token") == null){
            return null;
        }else {
            return body.get("token").toString();
        }
    }

    public  String refreshredis(String token){
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

}
