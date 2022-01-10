package org.zznode.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zznode.beans.MD5Utils;
import org.zznode.dao.*;
import org.zznode.dto.InsertUserinfoParam;
import org.zznode.dto.SerchUserInfoByaccountAndUsernameAndPhoneParam;
import org.zznode.entity.*;
import org.zznode.service.UsersService;
import org.zznode.util.SnowflakeIdGenerator;
import org.apache.commons.beanutils.ConvertUtils;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsersServiceImpl extends ServiceImpl<TbUsersMapper, TbUsers> implements UsersService {

    @Autowired
    private TbUsersMapper tbUsersMapper;

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Autowired
    private TbDepartmentMapper tbDepartmentMapper;

    @Autowired
    private TbParyMapper tbParyMapper;

    @Autowired
    private TbRolesMapper tbRolesMapper;

    @Autowired
    private TbRolesUsersMapper tbRolesUsersMapper;

    @Autowired
    private TbRolesRulesMapper tbRolesRulesMapper;

    public JSONArray serchUserInfoByaccountAndUsernameAndPhone(SerchUserInfoByaccountAndUsernameAndPhoneParam param){

        String account = null;
        if(null != param.getAccount()){
            account = param.getAccount().replace("%","\\%");
        }

        String username = null;
        if(null != param.getUsername()){
            username = param.getUsername().replace("%","\\%");
        }

        String phone = null;
        if(null != param.getPhone()){
            phone = param.getPhone().replace("%","\\%");
        }

        QueryWrapper<TbUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(account),"account",account)
                .like(StringUtils.isNotBlank(username),"userName",username)
                .like(StringUtils.isNotBlank(phone),"phone",phone)
                .ne("departID",999);
        List<TbUsers> users = tbUsersMapper.selectList(queryWrapper);
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(users));
        return jsonArray;
    }

    public JSONObject insertUserinfo(InsertUserinfoParam param) throws NoSuchAlgorithmException {
        JSONObject jsonRetu = new JSONObject();
        QueryWrapper<TbUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(param.getAccount()),"account",param.getAccount());
        List<TbUsers> userslist = tbUsersMapper.selectList(queryWrapper);
        if(userslist.size() > 0 ){
            jsonRetu.put("description","该账号已存在");
            jsonRetu.put("code","000002");
            jsonRetu.put("result","falt");
            return jsonRetu;
        }

        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(param.getNickname()),"nickname",param.getNickname());
        List<TbUsers> userss = tbUsersMapper.selectList(queryWrapper);
        if(userss.size() > 0 ){
            jsonRetu.put("description","该昵称已存在");
            jsonRetu.put("code","000002");
            jsonRetu.put("result","falt");
            return jsonRetu;
        }

        QueryWrapper<TbDepartment> departmentQueryWrapper = new QueryWrapper<>();
        departmentQueryWrapper.eq("departID",param.getDepartid());
        List<TbDepartment> departmentList =  tbDepartmentMapper.selectList(departmentQueryWrapper);
        if(departmentList.size() == 0){
            jsonRetu.put("description","行政部门不存在");
            jsonRetu.put("code","000002");
            jsonRetu.put("result","falt");
            return jsonRetu;
        }

        QueryWrapper<TbPary> paryQueryWrapper = new QueryWrapper<>();
        paryQueryWrapper.eq("partID",param.getParyid());
        List<TbPary> paryList = tbParyMapper.selectList(paryQueryWrapper);
        if(paryList.size() == 0){
            jsonRetu.put("description","党组织不存在");
            jsonRetu.put("code","000003");
            jsonRetu.put("result","falt");
            return jsonRetu;
        }

        if( null == param.getRolesid()  ||  param.getRolesid().isEmpty() ){
            System.out.println("rolesid is empty!");
        }else {
            List<Long> rolelists = ConvetDataType(param.getRolesid());
            for(Long rolesid:rolelists){
                QueryWrapper<TbRoles> rolesQueryWrapper = new QueryWrapper<>();
                rolesQueryWrapper.eq("rolesID", rolesid);
                List<TbRoles> rolesList = tbRolesMapper.selectList(rolesQueryWrapper);
                if (rolesList.size() == 0) {
                    jsonRetu.put("description", "角色不存在");
                    jsonRetu.put("code", "000004");
                    jsonRetu.put("result", "falt");
                    return jsonRetu;
                }
            }
        }

        TbUsers tbUsers = new TbUsers();
        String inputString = MD5Utils.getMD5Str(param.getPassword());
        tbUsers.setUserid(snowflakeIdGenerator.nextId());
        tbUsers.setAccount(param.getAccount());
        tbUsers.setPassword(inputString);
        tbUsers.setUsername(param.getUsername());
        tbUsers.setPhone(param.getPhone());
        tbUsers.setSex(param.getSex());
        tbUsers.setEmail(param.getEmail());
        tbUsers.setDepartid(param.getDepartid());
        tbUsers.setParyid(param.getParyid());
        tbUsers.setAccountstatus(param.getAcountstatus());
        tbUsers.setNickname(param.getNickname());
        tbUsers.setDepartdutyid(param.getDepartdutyid());
        tbUsers.setParydutyid(param.getParydutyid());
        tbUsersMapper.insertOne(tbUsers);

        if( null == param.getRolesid()  ||  param.getRolesid().isEmpty() ){
            System.out.println("rolesid is empty!");
        }else {
            List<Long> rolelists  = ConvetDataType(param.getRolesid());
            for(Long rolesid:rolelists){
                TbRolesUsers tbRolesUsers = new TbRolesUsers();
                tbRolesUsers.setUserid(tbUsers.getUserid());
                tbRolesUsers.setRolesid(rolesid);
                tbRolesUsers.setId(snowflakeIdGenerator.nextId());
                Date date = new Date();
                tbRolesUsers.setCreatetime(date);
                tbRolesUsersMapper.insertOne(tbRolesUsers);
            }
        }


        //将新增用户的组织职务插入到用户角色中间表
        if( null == tbUsers.getParydutyid() || tbUsers.getParydutyid().equals("")){
            System.out.println("Parydutyid is null");
        }else {
            QueryWrapper<TbRolesUsers> tbRolesUsersWrapper = new QueryWrapper<>();
            tbRolesUsersWrapper.eq("userID", tbUsers.getUserid()).eq("rolesID", tbUsers.getParydutyid());
            List<TbRolesUsers> tbRolesUserslist = tbRolesUsersMapper.selectList(tbRolesUsersWrapper);
            if (tbRolesUserslist.size() == 0) {
                TbRolesUsers tr = new TbRolesUsers();
                tr.setUserid(tbUsers.getUserid());
                tr.setRolesid(tbUsers.getParydutyid());
                tr.setId(snowflakeIdGenerator.nextId());
                Date date = new Date();
                tr.setCreatetime(date);
                tbRolesUsersMapper.insertOne(tr);
            }
        }

        //将新增用户的行政职务插入到用户角色中间表
        if( null == tbUsers.getDepartdutyid() || tbUsers.getDepartdutyid().equals("")){
            System.out.println("Departdutyid is null");
        }else {
            QueryWrapper<TbRolesUsers> tbRolesUsersWrapper = new QueryWrapper<>();
            tbRolesUsersWrapper.eq("userID", tbUsers.getUserid()).eq("rolesID", tbUsers.getDepartdutyid());
            List<TbRolesUsers> tbRolesUserslist = tbRolesUsersMapper.selectList(tbRolesUsersWrapper);
            if (tbRolesUserslist.size() == 0) {
                TbRolesUsers tr = new TbRolesUsers();
                tr.setUserid(tbUsers.getUserid());
                tr.setRolesid(tbUsers.getDepartdutyid());
                tr.setId(snowflakeIdGenerator.nextId());
                Date date = new Date();
                tr.setCreatetime(date);
                tbRolesUsersMapper.insertOne(tr);
            }
        }


        jsonRetu.put("description","成功");
        jsonRetu.put("code","000000");
        jsonRetu.put("result","success");
        return jsonRetu;
    }

    public JSONObject updateUserinfo(InsertUserinfoParam param) throws NoSuchAlgorithmException {
        JSONObject jsonRetu = new JSONObject();

        QueryWrapper<TbUsers> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq(StringUtils.isNotBlank(param.getAccount()),"account",param.getAccount());
        List<TbUsers> usersList = tbUsersMapper.selectList(userQueryWrapper);
        if(usersList.size() == 0){
            jsonRetu.put("description","用户账号不存在");
            jsonRetu.put("code","000004");
            jsonRetu.put("result","falt");
            return jsonRetu;
        }

        //清空角色
        tbRolesUsersMapper.deleteByuserID(usersList.get(0).getUserid());


        if(null != usersList.get(0).getNickname() && null != param.getNickname()  && !param.getNickname().equals("") && !usersList.get(0).getNickname().equals(param.getNickname())) {
            userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq(StringUtils.isNotBlank(param.getNickname()), "nickname", param.getNickname());
            List<TbUsers> userss = tbUsersMapper.selectList(userQueryWrapper);
            if (userss.size() > 0) {
                jsonRetu.put("description", "该昵称已存在");
                jsonRetu.put("code", "000002");
                jsonRetu.put("result", "falt");
                return jsonRetu;
            }
        }

        QueryWrapper<TbDepartment> departmentQueryWrapper = new QueryWrapper<>();
        departmentQueryWrapper.eq("departID",param.getDepartid());
        List<TbDepartment> departmentList =  tbDepartmentMapper.selectList(departmentQueryWrapper);
        if(departmentList.size() == 0){
            jsonRetu.put("description","行政部门不存在");
            jsonRetu.put("code","000002");
            jsonRetu.put("result","falt");
            return jsonRetu;
        }

        QueryWrapper<TbPary> paryQueryWrapper = new QueryWrapper<>();
        paryQueryWrapper.eq("partID",param.getParyid());
        List<TbPary> paryList = tbParyMapper.selectList(paryQueryWrapper);
        if(paryList.size() == 0){
            jsonRetu.put("description","党组织不存在");
            jsonRetu.put("code","000002");
            jsonRetu.put("result","falt");
            return jsonRetu;
        }

        if( null == param.getRolesid() || param.getRolesid().isEmpty()){
            System.out.println("rolesid is empty!");
        }else {
            List<Long> rolelists  = ConvetDataType(param.getRolesid());
            for(Long rolesid:rolelists){
                QueryWrapper<TbRoles> rolesQueryWrapper = new QueryWrapper<>();
                rolesQueryWrapper.eq("rolesID", rolesid);
                List<TbRoles> rolesList = tbRolesMapper.selectList(rolesQueryWrapper);
                if (rolesList.size() == 0) {
                    jsonRetu.put("description", "角色不存在");
                    jsonRetu.put("code", "000004");
                    jsonRetu.put("result", "falt");
                    return jsonRetu;
                }
            }
        }

        String inputString;
        TbUsers tbUsers = new TbUsers();
        if( null != param.getPassword() ) {
            inputString = MD5Utils.getMD5Str(param.getPassword());
        }else{
            inputString = usersList.get(0).getPassword();
        }
        tbUsers.setUserid(usersList.get(0).getUserid());
        tbUsers.setAccount(param.getAccount());
        tbUsers.setPassword(inputString);

        tbUsers.setUsername(param.getUsername());
        tbUsers.setPhone(param.getPhone());
        tbUsers.setSex(param.getSex());
        tbUsers.setEmail(param.getEmail());
        tbUsers.setDepartid(param.getDepartid());
        tbUsers.setParyid(param.getParyid());
        tbUsers.setAccountstatus(param.getAcountstatus());
        tbUsers.setNickname(param.getNickname());
        tbUsers.setDepartdutyid(param.getDepartdutyid());
        tbUsers.setParydutyid(param.getParydutyid());
        tbUsersMapper.updateByPrimaryKeySelective(tbUsers);

        if( null == param.getRolesid() || param.getRolesid().isEmpty()){
            System.out.println("rolesid is empty!");
        }else {
            List<Long> rolelists  = ConvetDataType(param.getRolesid());
            for(Long rolesid:rolelists){
                TbRolesUsers tbRolesUsers = new TbRolesUsers();
                tbRolesUsers.setUserid(tbUsers.getUserid());
                tbRolesUsers.setRolesid(rolesid);
                tbRolesUsers.setId(snowflakeIdGenerator.nextId());
                Date date = new Date();
                tbRolesUsers.setCreatetime(date);
                tbRolesUsersMapper.insertOne(tbRolesUsers);
            }
        }

        //将新增用户的组织职务、行政职务插入到用户角色中间表
        if( null == tbUsers.getParydutyid() || tbUsers.getParydutyid().equals("")){
            System.out.println("Parydutyid is null");
        }else {
            QueryWrapper<TbRolesUsers> tbRolesUsersWrapper = new QueryWrapper<>();
            tbRolesUsersWrapper.eq("userID", tbUsers.getUserid()).eq("rolesID", tbUsers.getParydutyid());
            List<TbRolesUsers> tbRolesUserslist = tbRolesUsersMapper.selectList(tbRolesUsersWrapper);
            if (tbRolesUserslist.size() == 0) {
                TbRolesUsers tr = new TbRolesUsers();
                tr.setUserid(tbUsers.getUserid());
                tr.setRolesid(tbUsers.getParydutyid());
                tr.setId(snowflakeIdGenerator.nextId());
                Date date = new Date();
                tr.setCreatetime(date);
                tbRolesUsersMapper.insertOne(tr);
            }
        }

        if( null == tbUsers.getDepartdutyid() || tbUsers.getDepartdutyid().equals("")){
            System.out.println("Departdutyid is null");
        }else {
            QueryWrapper<TbRolesUsers> tbRolesUsersWrapper = new QueryWrapper<>();
            tbRolesUsersWrapper.eq("userID", tbUsers.getUserid()).eq("rolesID", tbUsers.getDepartdutyid());
            List<TbRolesUsers> tbRolesUserslist = tbRolesUsersMapper.selectList(tbRolesUsersWrapper);
            if (tbRolesUserslist.size() == 0) {
                TbRolesUsers tr = new TbRolesUsers();
                tr.setUserid(tbUsers.getUserid());
                tr.setRolesid(tbUsers.getDepartdutyid());
                tr.setId(snowflakeIdGenerator.nextId());
                Date date = new Date();
                tr.setCreatetime(date);
                tbRolesUsersMapper.insertOne(tr);
            }
        }

        jsonRetu.put("description","成功");
        jsonRetu.put("code","000000");
        jsonRetu.put("result","success");
        return jsonRetu;
    }

    public JSONObject deleteUserinfo(Long userid){
        System.out.println("deleteUserinfo:" + userid);
        JSONObject jsonRetu = new JSONObject();
        QueryWrapper<TbUsers> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userID",userid);
        List<TbUsers> usersList = tbUsersMapper.selectList(userQueryWrapper);
        if(usersList.size() == 0){
            jsonRetu.put("description","用户账号不存在");
            jsonRetu.put("code","000002");
            jsonRetu.put("result","falt");
            return jsonRetu;
        }

        tbUsersMapper.deleteByPrimaryKey(usersList.get(0).getUserid()); //删除用户表
        tbRolesUsersMapper.deleteByuserID(usersList.get(0).getUserid()); //删除角色用户中间表
        jsonRetu.put("description","成功");
        jsonRetu.put("code","000000");
        jsonRetu.put("result","success");
        return jsonRetu;
    }

    public JSONObject serchUserinfo(JSONObject json){
        JSONObject jsonRetu = new JSONObject();
        System.out.println(json.getString("account"));
        QueryWrapper<TbUsers> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq(StringUtils.isNotBlank(json.getString("account")),"account",json.getString("account"));
        List<TbUsers> usersList = tbUsersMapper.selectList(userQueryWrapper);
        if(usersList.size() == 0){
            System.out.println("不存在");
            jsonRetu.put("description","用户账号不存在");
            jsonRetu.put("code","000002");
            jsonRetu.put("result","falt");
            return jsonRetu;
        }
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(usersList));
        System.out.println(jsonArray.toString());

        jsonRetu.put("description","成功");
        jsonRetu.put("code","000000");
        jsonRetu.put("result","success");
        jsonRetu.put("users",jsonArray.get(0));
        return jsonRetu;
    }

    public JSONObject serchRolesIDByUserID(Long userid){
        JSONObject jsonRetu = new JSONObject();

        QueryWrapper<TbRolesUsers> rolesUsersQueryWrapper = new QueryWrapper<>();
        rolesUsersQueryWrapper.eq("userID",userid);
        List<TbRolesUsers> rolesUsers = tbRolesUsersMapper.selectList(rolesUsersQueryWrapper);
        System.out.println("userid:" + userid + "rolesUsers:" + rolesUsers);
        if(rolesUsers.size() == 0){
            System.out.println("不存在");
//            jsonRetu.put("description","该用户对应权限为空");
//            jsonRetu.put("code","000001");
//            jsonRetu.put("result","falt");
            jsonRetu.put("authoritylist",null);
            return jsonRetu;
        }
        List<Long> authoritylist = new LinkedList<>();


        int i = 0;
        for(i=0;i<rolesUsers.size();i++){
            QueryWrapper<TbRolesRules>  rolesrules = new QueryWrapper<>();
            rolesrules.eq("rolesID",rolesUsers.get(i).getRolesid());
            List<TbRolesRules> tbRolesRules = tbRolesRulesMapper.selectList(rolesrules);
            int j = 0;
            for(j=0;j<tbRolesRules.size();j++){
                authoritylist.add(tbRolesRules.get(j).getRulesid());
            }
            System.out.println("tbRolesRules:" + tbRolesRules);
        }
        System.out.println("authoritylist:" + authoritylist);
        HashSet h = new HashSet(authoritylist);
        authoritylist.clear();
        authoritylist.addAll(h);
        System.out.println("authoritylist:" + authoritylist);

        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(rolesUsers));
        System.out.println(jsonArray.toString());

//        jsonRetu.put("description","成功");
//        jsonRetu.put("code","000000");
//        jsonRetu.put("result","success");
        jsonRetu.put("authoritylist",authoritylist);
        return jsonRetu;
    }

    private List<Long> ConvetDataType(String sourceData)
    {
        return Arrays.stream(sourceData.split(",")).map(s->Long.parseLong(s.trim())).collect(Collectors.toList());
    }
}
