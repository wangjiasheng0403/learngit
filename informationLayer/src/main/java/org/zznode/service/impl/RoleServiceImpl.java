package org.zznode.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zznode.dao.TbRolesMapper;
import org.zznode.dao.TbRolesRulesMapper;
import org.zznode.dao.TbRulesMapper;
import org.zznode.dto.InsertRoleInfoparam;
import org.zznode.dto.SerchRoleInfoByRoleNameOrDescriptorOrCreateTimeParam;
import org.zznode.dto.UpdateRoleInfoParam;
import org.zznode.entity.TbRoles;
import org.zznode.entity.TbRolesRules;
import org.zznode.entity.TbRules;
import org.zznode.service.RoleService;
import org.zznode.util.SnowflakeIdGenerator;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<TbRolesMapper, TbRoles> implements RoleService{

    @Autowired
    private TbRolesMapper tbRolesMapper;

    @Autowired
    private TbRulesMapper tbRulesMapper;

    @Autowired
    private TbRolesRulesMapper tbRolesRulesMapper;

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;


    @Override
    public List<TbRoles> serchRoleInfoAll(){
        QueryWrapper<TbRoles> queryWrapper = new QueryWrapper<>();
        List<TbRoles> roles =  tbRolesMapper.selectList(queryWrapper);

        return roles;
    }

    @Override
    public JSONArray serchRuleInfo() {
        QueryWrapper<TbRules> queryWrapper = new QueryWrapper<>();
        List<TbRules> tbRuleList = tbRulesMapper.selectList(queryWrapper);
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(tbRuleList));
        return jsonArray;
    }

    @Override
    public JSONObject insertRolest(InsertRoleInfoparam param){

        JSONObject json = new JSONObject();
        JSONObject jsonRole = new JSONObject();

        QueryWrapper<TbRoles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(param.getRolesname()),"rolesName",param.getRolesname());
        List<TbRoles> roles = tbRolesMapper.selectList(queryWrapper);
        if(roles.size() > 0){
            json.put("description","该角色名称已占用");
            json.put("code","000002");
            json.put("result","falt");
            return json;
        }

        jsonRole.put("rolesname",param.getRolesname());
        jsonRole.put("rolesid",snowflakeIdGenerator.nextId());

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonRole.put("createtime",format.format(date));
        jsonRole.put("descriptor",param.getDescriptor());

        System.out.println("insertRolest jsonRole:" + jsonRole);
        //JSON转OBJECT
        TbRoles td= JSON.parseObject(jsonRole.toJSONString(),new TypeReference<TbRoles>(){});
        tbRolesMapper.insertOne(td);

        List<Long> auth = param.getAuthority();
        if(null != auth){
            for(Long authority:auth){
                TbRules tbRules = tbRulesMapper.selectByPrimaryKey(authority);

                if( null ==  tbRules ){
                    json.put("description","所要添加的权限不存在:" +  authority);
                    json.put("code","000002");
                    json.put("result","falt");
                    return json;
                }
                TbRolesRules tbRolesRule = new TbRolesRules();
                tbRolesRule.setRolesid(jsonRole.getLong("rolesid"));
                tbRolesRule.setRulesid(authority);
                tbRolesRule.setCreatetime(date);
                tbRolesRule.setId(snowflakeIdGenerator.nextId());
                tbRolesRulesMapper.insertOne(tbRolesRule);
            }
        }

        json.put("description","成功");
        json.put("code","000000");
        json.put("result","success");
        return json;
    }

    @Override
    public JSONObject deleteRolestOne(JSONObject json) {
        JSONObject jsonRetu = new JSONObject();

        QueryWrapper<TbRoles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(json.getString("rolesname")),"rolesName",json.getString("rolesname"));
        List<TbRoles> roles = tbRolesMapper.selectList(queryWrapper);

        if(roles.size() == 0){
            jsonRetu.put("description","该角色不存在");
            jsonRetu.put("code","000002");
            jsonRetu.put("result","falt");
            return jsonRetu;
        }
        tbRolesMapper.deleteByPrimaryKey(roles.get(0).getRolesid() );

        jsonRetu.put("description","删除成功");
        jsonRetu.put("code","000000");
        jsonRetu.put("result","success");
        return jsonRetu;
    }

    @Override
    public JSONObject updateRoleInfo(UpdateRoleInfoParam param) {

        JSONObject json = new JSONObject();
        QueryWrapper<TbRoles> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("rolesID",param.getRolesid());
        List<TbRoles> troles = tbRolesMapper.selectList(queryWrapper);
        if(troles.size() == 0){
            json.put("description","所要修改的角色不存在");
            json.put("code","000000");
            json.put("result","success");
            return json;
        }

        if(!troles.get(0).getRolesname().equals(param.getRolesname())){
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(StringUtils.isNotBlank(param.getRolesname()),"rolesName",param.getRolesname());
            List<TbRoles> trolelist = tbRolesMapper.selectList(queryWrapper);
            if(trolelist.size() > 0){
                json.put("description","新的角色名已存在，请更换角色名");
                json.put("code","000000");
                json.put("result","success");
                return json;
            }
        }

        if(null == param.getRolesname() || param.getRolesname().isEmpty()){
            json.put("rolesname",troles.get(0).getRolesname());
        }else{
            json.put("rolesname",param.getRolesname());
        }
        json.put("rolesid",param.getRolesid());
        json.put("descriptor",param.getDescriptor());


        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        json.put("createtime",format.format(date));

        TbRoles td= JSON.parseObject(json.toJSONString(),new TypeReference<TbRoles>(){});
        tbRolesMapper.updateByPrimaryKeySelective(td);

        tbRolesRulesMapper.deleteByRolesID(json.getLong("rolesid"));
        List<Long> auth = param.getAuthority();
        if(null != auth){
            for(Long authority:auth){

                TbRules tbRules = tbRulesMapper.selectByPrimaryKey(authority);

                if( null ==  tbRules ){
                    json.put("description","所要添加的权限不存在:" +  authority);
                    json.put("code","000002");
                    json.put("result","falt");
                    return json;
                }
                TbRolesRules tbRolesRule = new TbRolesRules();
                tbRolesRule.setRolesid(json.getLong("rolesid"));
                tbRolesRule.setRulesid(authority);
                tbRolesRule.setCreatetime(date);
                tbRolesRule.setId(snowflakeIdGenerator.nextId());
                tbRolesRulesMapper.insertOne(tbRolesRule);
            }
        }

        json.put("description","修改成功");
        json.put("code","000000");
        json.put("result","success");
        return json;
    }

    @Override
    public JSONObject serchRoleInfoByRoleNameOrDescriptorOrCreateTime(SerchRoleInfoByRoleNameOrDescriptorOrCreateTimeParam param) {
        JSONObject json = new JSONObject();

        String rname = null;
        if(null != param.getRolesname()){
            rname = param.getRolesname().replace("%","\\%");
        }

        String descriptor = null;
        if(null != param.getRolesname()){
            descriptor = param.getDescriptor().replace("%","\\%");
        }

        QueryWrapper<TbRoles> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(rname),"rolesName",rname)
                .like(StringUtils.isNotBlank(descriptor),"descriptor",descriptor)
                .ge(StringUtils.isNotBlank(param.getStartTime()),"createTime",param.getStartTime())
                .le(StringUtils.isNotBlank(param.getEndTime()),"createTime",param.getEndTime());
        List<TbRoles> roles = tbRolesMapper.selectList(queryWrapper);
        if(roles.size() == 0){
            json.put("description","该角色不存在");
            json.put("code","000000");
            json.put("result","success");
            return json;
        }

        //OBJECTLIST转JSONArray
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(roles));
        JSONArray jsonArrayRetu = new JSONArray();
        int i;
        for( i=0;i<jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            List<TbRolesRules> tbRolesRules = tbRolesRulesMapper.selectByRolesID(jsonObject.getLong("rolesid"));

            JSONArray jsonruleArray = JSONArray.parseArray(JSON.toJSONString(tbRolesRules));
            JSONArray jsonruleRetu = new JSONArray();
            if(jsonruleArray.size() > 0) {
                int j;
                for (j = 0; j < jsonruleArray.size(); j++) {
                    JSONObject jsonRuleObject = jsonruleArray.getJSONObject(j);
                    QueryWrapper<TbRules> rulesWrapper = new QueryWrapper<>();
                    rulesWrapper.like("rulesID", jsonRuleObject.getLong("rulesid"));
                    List<TbRules> tbRules = tbRulesMapper.selectList(rulesWrapper);
                    if(tbRules.size() > 0) {
                        jsonRuleObject.put("rulesname", tbRules.get(0).getRulesname());
                    }else{
                        jsonRuleObject.put("rulesname", null);
                    }
                    jsonruleRetu.add(jsonRuleObject);
                }
                System.out.println("===>rulesList:" + jsonruleRetu);
                jsonObject.put("rulesList", jsonruleRetu);
            }else{
                jsonObject.put("rulesList", null);
            }
            jsonArrayRetu.add(jsonObject);
        }

        if(null == param.getPageIndex() || null == param.getPageSize() || param.getPageIndex() < 1 || param.getPageSize() <1){
            json.put("description","成功");
            json.put("code","000000");
            json.put("rules",jsonArrayRetu);
            json.put("total",jsonArrayRetu.size());
            json.put("result","success");
            return json;
        }


        int pageBegin =(param.getPageIndex() - 1) * param.getPageSize();
        int pageEnd = param.getPageIndex() * param.getPageSize();

        JSONArray retJsonarray = new JSONArray();
        if(pageBegin >= jsonArrayRetu.size()){
            json.put("result","file");
            json.put("code","000004");
            json.put("description","已经超出最大值");
            return json;
        }

        int ii = 0;
        for(ii=pageBegin; ii<pageEnd&&ii<jsonArrayRetu.size(); ii++){
            retJsonarray.add(jsonArrayRetu.get(ii));
        }

        System.out.println("jsonArrayRetu:" + retJsonarray);
        json.put("description","成功");
        json.put("code","000000");
        json.put("rules",retJsonarray);
        json.put("total",jsonArrayRetu.size());
        json.put("result","success");
        return json;
    }

    public JSONObject serchRoleInfoByRoleNameOrDescriptorOrCreateTime() {
        JSONObject json = new JSONObject();

        QueryWrapper<TbRoles> queryWrapper = new QueryWrapper<>();
        List<TbRoles> roles = tbRolesMapper.selectList(queryWrapper);
        if(roles.size() == 0){
            json.put("description","没有查询到角色数据");
            json.put("code","000000");
            json.put("result","success");
            return json;
        }

        //OBJECTLIST转JSONArray
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(roles));
        JSONArray jsonArrayRetu = new JSONArray();
        for( int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            List<TbRolesRules> tbRolesRules = tbRolesRulesMapper.selectByRolesID(jsonObject.getLong("rolesid"));
            JSONArray jsonruleArray = JSONArray.parseArray(JSON.toJSONString(tbRolesRules));
            JSONArray jsonruleRetu = new JSONArray();
            if(jsonruleArray.size() > 0) {
                for (int j = 0; j < jsonruleArray.size(); j++) {
                    JSONObject jsonRuleObject = jsonruleArray.getJSONObject(j);
                    QueryWrapper<TbRules> rulesWrapper = new QueryWrapper<>();
                    rulesWrapper.like("rulesID", jsonRuleObject.getLong("rulesid"));
                    List<TbRules> tbRules = tbRulesMapper.selectList(rulesWrapper);
                    if(tbRules.size() > 0) {
                        jsonRuleObject.put("rulesname", tbRules.get(0).getRulesname());
                    }else{
                        jsonRuleObject.put("rulesname", null);
                    }
                    jsonruleRetu.add(jsonRuleObject);

                }
                System.out.println("rulesList:" + jsonruleRetu);
                jsonObject.put("rulesList", jsonruleRetu);
            }else{
                jsonObject.put("rulesList", null);
            }
            jsonArrayRetu.add(jsonObject);
        }

        json.put("description","成功");
        json.put("code","000000");
        json.put("rules",jsonArrayRetu);
        json.put("total",jsonArrayRetu.size());
        json.put("result","success");
        return json;
    }
}
