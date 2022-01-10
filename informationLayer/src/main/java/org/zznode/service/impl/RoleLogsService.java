package org.zznode.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.zznode.dao.TbDataResourceMapper;
import org.zznode.dao.TbMakesMapper;
import org.zznode.dao.TbRoleLogsMapper;
import org.zznode.dao.TbUsersMapper;
import org.zznode.dto.*;
import org.zznode.entity.TbDataResource;
import org.zznode.entity.TbMakes;
import org.zznode.entity.TbRoleLogs;
import org.zznode.entity.TbUsers;
import org.zznode.util.SnowflakeIdGenerator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author san
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleLogsService {

    private final TbRoleLogsMapper roleLogsMapper;
    private final TbDataResourceMapper dataResourceMapper;
    private final TbMakesMapper makesMapper;
    private final TbUsersMapper usersMapper;

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;
    /**
     * 保存 roleLogs
     *
     * @param param param
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(RoleLogParam param) {
        TbUsers tbUsers = usersMapper.selectById(param.getUserId());
        TbRoleLogs roleLogs = roleLogsMapper.selectById(param.getId());
        if (roleLogs == null) {
            roleLogs = new TbRoleLogs();
            initRoleLogs(roleLogs, param);
            roleLogs.setId(Long.parseLong(param.getId()));
            roleLogsMapper.insert(roleLogs);
        } else {
            initRoleLogs(roleLogs, param);
            roleLogsMapper.updateById(roleLogs);
        }

        if (StringUtils.isNotEmpty(param.getMake())) {
            TbMakes tbMake = new TbMakes();
            tbMake.setId(snowflakeIdGenerator.nextId());
            tbMake.setLogsid(Long.parseLong(param.getId()));
            tbMake.setMakeInfo(param.getMake());
            tbMake.setCtime(new Date());
            tbMake.setNickname(tbUsers.getNickname());
            makesMapper.insert(tbMake);
        }
    }

    /**
     * 浏览游客访问报表
     *
     * @param param param
     * @return list
     */
    public Page<Map<String, Object>> getRoleLogs(GetRoleLogsParam param) {
        initParamDate(param);
        return roleLogsMapper.getRoleLogs(new Page<>(param.getPageNo(), param.getPageSize()), param);
    }

    /**
     * 场景列表
     *
     * @return list
     */
    public Page<Map<String, Object>> getDataResource(DataResourceParam param) {
        return roleLogsMapper.getDataResource(new Page<>(param.getPageNo(), param.getPageSize()));
    }

    /**
     * 访问明细
     *
     * @param param param
     * @return list
     */
    public Page<Map<String, Object>> visitingDetail(VisitingDetailParam param) {
        initParamDate(param);
        return roleLogsMapper.visitingDetail(new Page<>(param.getPageNo(), param.getPageSize()), param);
    }

    /**
     * init rolelogs
     *
     * @param roleLogs roleLogs
     * @param param    param
     */
    private void initRoleLogs(TbRoleLogs roleLogs, RoleLogParam param) {
        TbDataResource dataResource = dataResourceMapper.selectById(param.getDataId());
        roleLogs.setId(Long.parseLong(param.getId()));
        roleLogs.setUserid(Long.parseLong(param.getUserId()));
        if (ObjectUtils.isEmpty(dataResource)) {
            roleLogs.setDataid(roleLogs.getDataid());
        } else {
            roleLogs.setDataname(dataResource.getDataname());
        }
        roleLogs.setDataid(param.getDataId());
        roleLogs.setCtime(new Date());
        roleLogs.setLiked(param.getLike());
        roleLogs.setCollect(param.getCollect());
        roleLogs.setTerminaltype(param.getTerminalType());
    }

    /**
     * 格式化查询日期
     *
     * @param param param
     */
    private void initParamDate(QueryDateParam param) {
        if (StringUtils.isEmpty(param.getStartTime())) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            param.setStartTime(df.format(new Date()));
            param.setEndTime(df.format(new Date()));
        }
        param.setStartTime(param.getStartTime() + " 00:00:00");
        param.setEndTime(param.getEndTime() + " 23:59:59");
    }

    /**
     * 终端设备用户行为列表
     *
     * @param param
     * @return
     */
    public Page<Map<String, Object>> getRoleLogsList(RoleLogsSeachList param) {
        param.setStartTime(param.getStartTime());
        param.setEndTime(param.getEndTime());
//        initParamDate(param);
        if (StringUtils.isEmpty(param.getStartTime())) {
            Calendar cal = Calendar.getInstance();
            //获取本月第一天的日期
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            String firstDay = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            //获取本月最后一天的日期
            String endDay = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
            param.setStartTime(firstDay);
            param.setEndTime(endDay);
        }
        return roleLogsMapper.getRolesLogList(new Page<>(param.getPageNo(), param.getPageSize()), param);
    }

    /**
     * 登录场景记录
     *
     * @param param
     * @return
     */
    public Page<Map<String, Object>> getLoginViewRecords(RoleLogsSearch param) {
        return roleLogsMapper.getLoginViewRecords(new Page<>(param.getPageNo(), param.getPageSize()), param);
    }

    /**
     * 场景内操作记录
     *
     * @param param
     * @return
     */
    public Page<Map<String, Object>> getOperateRecords(RoleLogsSearch param) {
        return roleLogsMapper.getOperateRecords(new Page<>(param.getPageNo(), param.getPageSize()), param);
    }

    /**
     * 查看评论
     *
     * @param param param
     * @return list
     */
    public Page<Map<String, Object>> viewComments(ViewCommentsParam param) {
        initParamDate(param);
        return roleLogsMapper.viewComments(new Page<>(param.getPageNo(), param.getPageSize()), param);
    }

    /**
     * 评论详细
     *
     * @param param param
     * @return list
     */
    public List<Map<String, Object>> commentsDetail(CommentsDetailParam param) {
        initParamDate(param);
        return roleLogsMapper.commentsDetail(param);
    }
}
