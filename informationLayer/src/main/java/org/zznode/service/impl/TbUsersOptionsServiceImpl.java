package org.zznode.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zznode.dao.TbRoleLogsMapper;
import org.zznode.dao.TbUsersMapper;
import org.zznode.dao.TbUsersOptionsMapper;
import org.zznode.dto.RoleLogParam;
import org.zznode.dto.UsersOptionParam;
import org.zznode.dto.UsersOptionSearch;
import org.zznode.entity.TbRoleLogs;
import org.zznode.entity.TbUsers;
import org.zznode.entity.TbUsersOptions;
import org.zznode.service.TbUsersOptionsService;
import org.zznode.service.UsersService;
import org.zznode.util.SnowflakeIdGenerator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @description 针对表【tb_users_options(用户操作日志表)】的数据库操作Service实现
 * @createDate 2021-12-10 13:45:10
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TbUsersOptionsServiceImpl extends ServiceImpl<TbUsersOptionsMapper, TbUsersOptions> implements TbUsersOptionsService {

    private final TbUsersOptionsMapper tbUsersOptionsMapper;
    private final TbUsersMapper tbUsersMapper;

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;
    /**
     * 保存 tb_users_options
     *
     * @param param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UsersOptionParam param) {

        //通过userId 查询account
        TbUsers tbUsers = tbUsersMapper.selectById(param.getUserId());
        //通过userId 查询account
        TbUsers tbModifyUsers = tbUsersMapper.selectById(param.getModifUserId());
        TbUsersOptions entity = new TbUsersOptions();
        entity.setId(snowflakeIdGenerator.nextId());//
        entity.setUserid(tbUsers.getUserid());//用户ID
        entity.setAccount(tbUsers.getAccount());//用户账号
        entity.setModifuserid(tbModifyUsers.getUserid());//被修改用户ID
        entity.setModifaccount(tbModifyUsers.getAccount());//被修改用户账号
        entity.setOptiontype(StringUtils.isEmpty(param.getOptionType())?"系统用户修改":param.getOptionType());//操作类型;定义增删改动作
        entity.setOptionbymenu(StringUtils.isEmpty(param.getOptionByMenu())?"系统用户管理":param.getOptionByMenu());//操作菜单;操作发生在后台具体菜单名
        entity.setOptionbyfield(param.getOptionByField());//操作字段;操作发生在菜单的字段
        entity.setOptioninfo(param.getOptionInfo());//操作内容;操作具体内容（如修改字段值，记录修改后值的内容）
        entity.setCreatetime(new Date());//操作时间
        tbUsersOptionsMapper.insert(entity);
    }


    /**
     * 显示用户信息修改记录 tb_users_options
     *
     * @param param param
     */
    @Override
    public Page<Map<String, Object>> getPageList(UsersOptionSearch param){
        //获取本月第一天的日期
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        String firstDay = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        //获取本月最后一天的日期
        String endDay = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
//        param.setStartTime(firstDay);
//        param.setEndTime(endDay);
        return tbUsersOptionsMapper.getPageList(new Page<>(param.getPageNo(), param.getPageSize()), param);
    }
}
