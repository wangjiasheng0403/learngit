package org.zznode.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.zznode.common.CommonResult;
import org.zznode.common.CustomException;
import org.zznode.config.RestTemplateConfig;
import org.zznode.dao.TbParyMapper;
import org.zznode.dao.TbRolesMapper;
import org.zznode.dao.TbRolesUsersMapper;
import org.zznode.dao.TbUsersMapper;
import org.zznode.dto.TbRolesParam;
import org.zznode.dto.TbUserParam;
import org.zznode.dto.TbUserSearch;
import org.zznode.entity.*;
import org.zznode.util.MD5Utils;
import org.zznode.util.RecordProxy;
import org.zznode.util.SnowflakeIdGenerator;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TbUsersService {

    private final TbUsersMapper tbUsersMapper;
    private final TbRolesUsersMapper tbRolesUsersMapper;
    private final TbParyMapper tbParyTreeMapper;
    private final TbRolesMapper tbRolesMapper;
    private final TokenService tokenService;
    private final RestTemplateConfig restTemplateConfig;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    /**
     * 获取分页列表
     *
     * @return 分页列表
     */
    public Page<Map<String, Object>> getPageList(TbUserSearch param) {
        return tbUsersMapper.getPageList(new Page<>(param.getPageNo(), param.getPageSize()), param);
    }

    /**
     * 通过主键查询明细
     */
    public Map<String, String> selectById(Long userId) {
        return tbUsersMapper.selectInfoById(userId);
    }

    /**
     * 查询所有角色
     */
    public List<TbRoles> getRoles() {
        return tbRolesMapper.selectList(null);
    }

    /**
     * 通过主键查询明细
     */
    public Integer updateStatus(Long userId, String accountStatus) {
        TbUsers tbUsers = new TbUsers();
        tbUsers.setUserid(userId);
        tbUsers.setAccountstatus(accountStatus);
        return tbUsersMapper.updateById(tbUsers);
    }

    /**
     * 党组织树形结构
     */
    public List<TbParyTree> paryTree() {
        return tbParyTreeMapper.paryTree(null);
    }

    /**
     * 根据id获取信息
     *
     * @param id 主键id
     * @return 详细信息
     */
    public Map<String, Object> getById(long id) {
        Map<String, Object> result = new HashMap<>();
        // 基本信息
        result.put("info", tbUsersMapper.selectInfoById(id));
        return result;
    }

    /**
     * 保存运营账号信息
     *
     * @param param 运营账号参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(TbUserParam param) {
        TbUsers users = new TbUsers();
        Long userId = snowflakeIdGenerator.nextId();
        users.setUserid(userId);
        users.setParyid(param.getParyId());
        try {
            users.setPassword(MD5Utils.getMD5Str(param.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        users.setAccount(param.getAccount());
        users.setUsername(param.getUsername());
        users.setPhone(param.getPhone());
        users.setEmail(param.getEmail());
        users.setSex(param.getSex());
        users.setAccountstatus(param.getAccountStatus());
        users.setDepartid(999L);
        tbUsersMapper.insert(users);
        if (!ObjectUtils.isEmpty(param.getRolesList()) && param.getRolesList().size() > 0) {
            for (TbRolesParam roleParam : param.getRolesList()) {
                TbRolesUsers tbRolesUsers = new TbRolesUsers();
                tbRolesUsers.setId(snowflakeIdGenerator.nextId());
                tbRolesUsers.setUserid(userId);
                tbRolesUsers.setRolesid(roleParam.getRolesId());
                tbRolesUsersMapper.insert(tbRolesUsers);
            }
        }
    }

    /**
     * 修改运营账号信息
     *
     * @param param 运营账号参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(TbUserParam param) {
        TbUsers users = new TbUsers();
        users.setUserid(param.getUserId());
        users.setParyid(param.getParyId());
        try {
            users.setPassword(MD5Utils.getMD5Str(param.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        users.setAccount(param.getAccount());
        users.setUsername(param.getUsername());
        users.setPhone(param.getPhone());
        users.setEmail(param.getEmail());
        users.setSex(param.getSex());
        users.setAccountstatus(param.getAccountStatus());
        users.setDepartid(999L);
        tbUsersMapper.updateById(users);
        tbRolesUsersMapper.delete(new QueryWrapper<TbRolesUsers>().lambda().in(TbRolesUsers::getUserid, users.getUserid()));
        if (!ObjectUtils.isEmpty(param.getRolesList()) && param.getRolesList().size() > 0) {
            Long userId = users.getUserid();
            for (TbRolesParam roleParam : param.getRolesList()) {
                TbRolesUsers tbRolesUsers = new TbRolesUsers();
                tbRolesUsers.setId(snowflakeIdGenerator.nextId());
                tbRolesUsers.setUserid(userId);
                tbRolesUsers.setRolesid(roleParam.getRolesId());
                tbRolesUsersMapper.insert(tbRolesUsers);
            }
        }
    }

    /**
     * 获取登录人所在组织信息
     */
    public Map<String, String> getParyByuserId(Long userId) {
        return tbUsersMapper.getParyByuserId(userId);
    }

    /**
     * 验证账号和手机号唯一性
     */
    public CommonResult<?> selectUnique(TbUserParam param) {
        QueryWrapper<TbUsers> queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("account", param.getAccount());
        if (param.getUserId() != null) {
            queryWrapper1.ne("userID", param.getUserId());
        }
        int accountCount = tbUsersMapper.selectCount(queryWrapper1);
        if (accountCount > 0) {
            return CommonResult.error(new CustomException("601", "用户名已存在", "该账号已存在！"));
        }

        QueryWrapper<TbUsers> queryWrapper2 = new QueryWrapper<TbUsers>();
        queryWrapper2.eq("phone", param.getPhone());
        if (param.getUserId() != null) {
            queryWrapper2.ne("userID", param.getUserId());
        }
        int phoneCount = tbUsersMapper.selectCount(queryWrapper2);
        if (phoneCount > 0) {
            return CommonResult.error(new CustomException("601", "手机号已存在", "该手机号已存在！"));
        }

        //验证密码是否符合规则
        if (!checkPWD(param.getPassword())) {
            return CommonResult.error(new CustomException("000002", "密码设置不合法，密码必须包含大小写字母、数字、特殊符号", "密码设置不合法，密码必须包含大小写字母、数字、特殊符号！"));
        }

        if (param.getUserId() != null) {
            Map<String, String> modifyUserMap = tbUsersMapper.selectInfoById(param.getUserId());//查询被修改人信息
            this.update(param);
            this.usersOptionSave(param, modifyUserMap);
        } else {
            this.save(param);
        }
        return CommonResult.success();
    }

    /**
     * 插入用户记录表
     *
     * @param oldParam 页面实体
     */
    public void usersOptionSave(TbUserParam oldParam, Map<String, String> modifyUserMap) {
        TbUserParam resultUsers = new TbUserParam();
        resultUsers.setToken(oldParam.getToken());
        resultUsers.setUserId(Long.parseLong(String.valueOf(modifyUserMap.get("UserID"))));
        resultUsers.setAccount(modifyUserMap.get("Account"));
        resultUsers.setAccountStatus(modifyUserMap.get("AccountStatus"));
        resultUsers.setEmail(modifyUserMap.get("Email"));
        resultUsers.setParyId(Long.parseLong(String.valueOf(modifyUserMap.get("partID"))));
        resultUsers.setPhone(modifyUserMap.get("Phone"));
        resultUsers.setSex(modifyUserMap.get("Sex"));
        resultUsers.setUsername(modifyUserMap.get("UserName"));
        resultUsers.setPassword(modifyUserMap.get("passWord"));
        resultUsers.setDuty(oldParam.getDuty());
        List<TbRolesParam> rolesList = new ArrayList<>();
        if (!StringUtils.isEmpty(modifyUserMap.get("rolesId"))) {
            List<String> tempList = Arrays.asList(modifyUserMap.get("rolesId").split(","));
            for (String value : tempList) {
                TbRolesParam roles = new TbRolesParam();
                roles.setRolesId(Long.parseLong(String.valueOf(value)));
                rolesList.add(roles);
            }
        }
        //用户修改记录数据准备
        Map<String, String> modityMap = RecordProxy.addRecord(resultUsers, oldParam);
        resultUsers.setRolesList(rolesList);
        if (!ObjectUtils.isEmpty(modityMap)) {
            UsersOptionParam entity = new UsersOptionParam();
            entity.setUserId(tokenService.getUserId(oldParam.getToken()));//登录用户ID
            entity.setModifUserId(oldParam.getUserId());//被修改人ID
            entity.setOptionType("修改");
            entity.setOptionByMenu("运营账号管理");
            entity.setOptionByField(modityMap.get("name"));
            entity.setOptionInfo(modityMap.get("value"));
            restTemplateConfig.getRestTemplate().postForEntity("http://informationLayer/usersOptions/save", entity, JSONObject.class).getBody();
        }
    }

    public Boolean checkPWD(String pwd) {

        //同时包含数字，字母，特殊符号
        String pattern = "^^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*_-]+$)(?![a-zA-z\\d]+$)(?![a-zA-z!@#$%^&*_-]+$)(?![\\d!@#$%^&*_-]+$)[a-zA-Z\\d!@#$%^&*_-]+$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(pwd);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }
}
