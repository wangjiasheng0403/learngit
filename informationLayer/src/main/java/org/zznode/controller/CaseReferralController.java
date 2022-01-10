package org.zznode.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.zznode.dao.TbDataResourceMapper;
import org.zznode.entity.CaseReferrer;
import org.zznode.dao.TbRoleLogsMapper;
import org.zznode.entity.TbDataResource;
import org.zznode.util.StaticUtils;

import java.util.*;

@CrossOrigin
@RequestMapping(value = "/recommend")
@Controller
@Api(tags = "案例推荐接口")
public class CaseReferralController {

    @Autowired
    private TbRoleLogsMapper tbRoleLogsMapper;

    @Autowired
    private TbDataResourceMapper tbDataResourceMapper;

    @Autowired
    private RestTemplate restTemplate;


    @Value("${prov.limit}")
    private int provlimit;

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "getCaseList",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "查询推荐案例", notes = "将访问次数超过配置数的公开党建资源按打分排序返回", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject getCaseList(@RequestHeader("Authorization") String token){
  		List<String> list = new ArrayList<String>();
         /*
        *        select a.dataID, d.dataName, d.dataPath, d.IMGPath, a.acount, a.average
FROM
(
SELECT r.dataID as dataID, count(*) as acount,
    sum(r.collect)/count(r.collect) as average
    FROM
	    tb_role_logs r
    WHERE
				1 = 1
    GROUP BY
	    r.dataID
    having acount >= #{provlimit,jdbcType=INTEGER}
)a, tb_data_resource d
where a.dataID = d.dataID and d.dataType = 2  and d.showtype =1
order by average desc
        * */
        JSONObject json = new JSONObject();
        if(this.checkToken(token)){
            List<CaseReferrer> caseReferrerList = tbRoleLogsMapper.getCaseReferrerResultList(provlimit);
            JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(caseReferrerList));
            json.put("cases",jsonArray);
            json.put("description","成功");
            json.put("code","000000");
            json.put("result","success");
            return json;
        }else{
            json.put("description","token无效");
            json.put("code","000001");
            json.put("result","falt");
            return json;
        }
    }


    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "getCaseListByHandSingle",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "单个手动调整案例排名", notes = "单个手动调整案例排名", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "案例ID", name = "dataID", dataType = "String", required = true,paramType = "query"),
            @ApiImplicitParam(value = "案例排名", name = "index", dataType = "int", required = true,paramType = "query")
    })
    public JSONObject getCaseListByHandSingle(@RequestHeader("Authorization") String token,String dataID,Integer index){
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            List<CaseReferrer> caseReferrerList = tbRoleLogsMapper.getCaseReferrerResultList(provlimit);

            List<CaseReferrer> caseReferrerListNew = new ArrayList<>();
            for (int i = 0; i < caseReferrerList.size(); i++) {

                if (index - 1 != i && !caseReferrerList.get(i).getDataid().equals(dataID)) {
                    caseReferrerListNew.add(caseReferrerList.get(i));
                }
                if (index - 1 == i && caseReferrerList.get(i).getDataid().equals(dataID)) {
                    caseReferrerListNew.add(caseReferrerList.get(i));
                }
                if (index - 1 == i && !caseReferrerList.get(i).getDataid().equals(dataID)) {
                    System.out.println(" index:" + index);
                    for (int j = 0; j < caseReferrerList.size(); j++) {
                        // System.out.println(" dataID:" + dataID + "|getDataid:" +  caseReferrerList.get(j).getDataid());
                        if (caseReferrerList.get(j).getDataid().equals(dataID)) {
                            System.out.println("dataID:" + caseReferrerList.get(j).getDataid());
                            caseReferrerListNew.add(caseReferrerList.get(j));
                        }
                    }
                    caseReferrerListNew.add(caseReferrerList.get(i));
                }

                if (caseReferrerList.get(i).getDataid().equals(dataID) && index - 1 != i) {
                    System.out.println(" i:" + i);
                }
            }


            JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(caseReferrerListNew));
            json.put("cases", jsonArray);
            json.put("description", "成功");
            json.put("code", "000000");
            json.put("result", "success");
            return json;
        }else{
            json.put("description","token无效");
            json.put("code","000001");
            json.put("result","falt");
            return json;
        }
    }


    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @ResponseBody
    @RequestMapping(value = "getCaseListByHand",method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "批量手动调整案例排名", notes = "批量手动调整案例排名", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "批量排序列表类型如{sn:dataid}", name = "map", dataType = "map", required = true,paramType = "body")
    })
    public JSONObject getCaseListByHand(@RequestHeader("Authorization") String token,@RequestBody  Map<String, String> map){
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            List<CaseReferrer> caseReferrerList = tbRoleLogsMapper.getCaseReferrerResultList(provlimit);
            List<CaseReferrer> caseReferrerListNew = new ArrayList<>();

            int index = 0;
            String dataID = "";

            for (String s : map.keySet()) {
                System.out.println("key:" + s);
                System.out.println("values:" + map.get(s));
                index = Integer.valueOf(s);
                dataID = map.get(s);

                for (int i = 0; i < caseReferrerList.size(); i++) {
                    //System.out.println(" --->i:" + i );
                    if (index - 1 != i && !caseReferrerList.get(i).getDataid().equals(dataID)) {
                        caseReferrerListNew.add(caseReferrerList.get(i));
                    }

                    if (index - 1 == i && caseReferrerList.get(i).getDataid().equals(dataID)) {
                        caseReferrerListNew.add(caseReferrerList.get(i));
                    }

                    if (index - 1 == i && !caseReferrerList.get(i).getDataid().equals(dataID)) {
                        for (int j = 0; j < caseReferrerList.size(); j++) {
                            // System.out.println(" dataID:" + dataID + "|getDataid:" +  caseReferrerList.get(j).getDataid());
                            if (caseReferrerList.get(j).getDataid().equals(dataID)) {
                                System.out.println("dataID:" + caseReferrerList.get(j).getDataid());
                                caseReferrerListNew.add(caseReferrerList.get(j));
                            }
                        }
                        caseReferrerListNew.add(caseReferrerList.get(i));
                    }

                    if (caseReferrerList.get(i).getDataid().equals(dataID) && index - 1 != i) {
                        System.out.println(" i:" + i);
                    }
                }

                caseReferrerList = caseReferrerListNew;
                caseReferrerListNew = new ArrayList<>();
            }

            JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(caseReferrerList));
            if (jsonArray.size() > 0) {
                tbDataResourceMapper.updateApriveOrder();
            }

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonTemp = (JSONObject) jsonArray.get(i);
                System.out.println("jsonTemp:" + jsonTemp);
                String dataid = jsonTemp.getString("dataid");
                TbDataResource tbd = tbDataResourceMapper.selectByPrimaryKey(dataid);
                tbd.setApriveorder(i + 1);
                System.out.println("tbd:" + tbd);
                tbDataResourceMapper.updateByPrimaryKey(tbd);
            }
            json.put("cases", jsonArray);
            json.put("description", "成功");
            json.put("code", "000000");
            json.put("result", "success");
            return json;
        }else{
            json.put("description","token无效");
            json.put("code","000001");
            json.put("result","falt");
            return json;
        }
    }

    //=================================提供给展示门户的统计工具=========================================//
    //===============================================================================================//
    /**
    * 获取观看次数及点赞次数的方法
    **/
    public JSONObject serch(String dataid){
        Map<String,Integer> m =  tbRoleLogsMapper.serchLikedByDataID(dataid);
        JSONObject json =new JSONObject();
        if(null == m){
            json.put("countW",0);
            json.put("countL",0);
        }else{
            json.put("countW",m.get("countW"));
            json.put("countL",m.get("countL"));
        }
        return json;
    }

    /**
    *获取排名前10的推荐资源
    **/
    @ResponseBody
    @GetMapping(value = "serchRecommendData")
    public JSONArray serchRecommendData(Long userid){
        JSONArray jsonArray = new JSONArray();

        List<TbDataResource> tbDataResourceList =  tbDataResourceMapper.getRecommendData();
        for(int i=0;i<tbDataResourceList.size();i++){

            JSONObject json = new JSONObject();

            TbDataResource tbd =  tbDataResourceList.get(i);
            JSONObject jb = serch(tbd.getDataid());

            boolean collectType = false;
            if(userid != 0){
                String datapath = tbd.getDatapath();
                int type = tbDataResourceMapper.getUserCollect(userid,datapath);
                if(type > 0){
                    collectType =true;
                }
            }

            json.put("dataid",tbd.getDataid());
            json.put("name",tbd.getDataname());
            json.put("imgPath",tbd.getImgpath());
            json.put("ModelPath",tbd.getDatapath());
            json.put("collectType",collectType);
            json.put("watch",jb.getInteger("countW"));
            json.put("like",jb.getInteger("countL"));
            jsonArray.add(json);
        }
        System.out.println("RecommendData:" + jsonArray);
        return jsonArray;
    }

    /**
    * 按组织部门获取部门所有场景的方法
    **/
    @ResponseBody
    @GetMapping(value = "serchPartydSceneData")
    public JSONArray serchPartydSceneData(Long paryid,Long userid){
        JSONArray jsonArray = new JSONArray();
        System.out.println("paryid:" + paryid);
        List<TbDataResource> tbDataResourceList =  tbDataResourceMapper.getPartydSceneData(paryid);
        for(int i=0;i<tbDataResourceList.size();i++){

            JSONObject json = new JSONObject();

            TbDataResource tbd =  tbDataResourceList.get(i);
            JSONObject jb = serch(tbd.getDataid());

            boolean collectType = false;
            if(userid != 0){
                String datapath =tbd.getDatapath();
               int type = tbDataResourceMapper.getUserCollect(userid,datapath);
               if(type > 0){
                   collectType =true;
               }
            }

            json.put("dataid",tbd.getDataid());
            json.put("name",tbd.getDataname());
            json.put("imgPath",tbd.getImgpath());
            json.put("ModelPath",tbd.getDatapath());
            json.put("collectType",collectType);
            json.put("watch",jb.getInteger("countW"));
            json.put("like",jb.getInteger("countL"));
            jsonArray.add(json);
        }
        return jsonArray;
    }

    /**
    * 获取所有公开党建资源的方法
    **/
    @GetMapping(value = "serchOpenData")
    @ResponseBody
    public JSONArray serchOpenData(Long userid){
        JSONArray jsonArray = new JSONArray();
        List<TbDataResource> tbDataResourceList =  tbDataResourceMapper.getOpenData();
        for(int i=0;i<tbDataResourceList.size();i++){

            JSONObject json = new JSONObject();

            TbDataResource tbd =  tbDataResourceList.get(i);
            JSONObject jb = serch(tbd.getDataid());
            boolean collectType = false;
            if(userid != 0){
                String datapath =tbd.getDatapath();
                int type = tbDataResourceMapper.getUserCollect(userid,datapath);
                if(type > 0){
                    collectType =true;
                }
            }

            json.put("dataid",tbd.getDataid());
            json.put("name",tbd.getDataname());
            json.put("imgPath",tbd.getImgpath());
            json.put("ModelPath",tbd.getDatapath());
            json.put("collectType",collectType);
            json.put("watch",jb.getInteger("countW"));
            json.put("like",jb.getInteger("countL"));
            jsonArray.add(json);
        }
        return jsonArray;
    }

    /**
    获取所有红色基地资源的方法
    **/
    @ResponseBody
    @GetMapping(value = "serchRedData")
    public JSONArray serchRedData(Long userid){
        JSONArray jsonArray = new JSONArray();
        List<TbDataResource> tbDataResourceList =  tbDataResourceMapper.getRedData();
        for(int i=0;i<tbDataResourceList.size();i++){

            JSONObject json = new JSONObject();

            TbDataResource tbd =  tbDataResourceList.get(i);
            JSONObject jb = serch(tbd.getDataid());

            boolean collectType = false;
            if(userid != 0){
                String datapath =tbd.getDatapath();
                int type = tbDataResourceMapper.getUserCollect(userid,datapath);
                if(type > 0){
                    collectType =true;
                }
            }

            json.put("dataid",tbd.getDataid());
            json.put("name",tbd.getDataname());
            json.put("imgPath",tbd.getImgpath());
            json.put("ModelPath",tbd.getDatapath());
            json.put("collectType",collectType);
            json.put("watch",jb.getInteger("countW"));
            json.put("like",jb.getInteger("countL"));
            jsonArray.add(json);
        }
        return jsonArray;
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
