package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import org.zznode.dto.*;
import org.zznode.entity.CaseReferrer;
import org.zznode.entity.TbRoleLogs;

import java.util.List;
import java.util.Map;

@Service
public interface TbRoleLogsMapper extends BaseMapper<TbRoleLogs> {
    int deleteByPrimaryKey(Integer id);

    int insertOne(TbRoleLogs record);

    int insertSelective(TbRoleLogs record);

    TbRoleLogs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbRoleLogs record);

    int updateByPrimaryKey(TbRoleLogs record);

    @Select("select count(*) as logincount from tb_users_options where userID =  #{userid} and date_format(SYSDATE(),'%Y-%m-%d') = date_format(createTime,'%Y-%m-%d')")
    int selectLogincount(Long userid);

    @Select("select count(*) as witchcount from tb_role_logs where userID = #{userid}  and date_format(SYSDATE(),'%Y-%m-%d') = date_format(cTime,'%Y-%m-%d')")
    int selectWatchcount(Long userid);

    @Select("select count(*) as makecount from tb_makes where logsid in\n" +
            "(select id from tb_role_logs where userID = #{userid} and date_format(SYSDATE(),'%Y-%m-%d') = date_format(cTime,'%Y-%m-%d'))")
    int selectMakecount(Long userid);


    @Select("select count(*) as logincount from tb_users_options where userID =  #{userid} and DATE_FORMAT(SYSDATE()- INTERVAL 1 DAY,'%Y-%m-%d') = DATE_FORMAT(createtime,'%Y-%m-%d') ")
    int selectLogincountByyesterday(Long userid);

    @Select("select count(*) as witchcount from tb_role_logs where userID = #{userid}  and DATE_FORMAT(SYSDATE()- INTERVAL 1 DAY,'%Y-%m-%d') = DATE_FORMAT(cTime,'%Y-%m-%d') ")
    int selectWatchcountByyesterday(Long userid);

    @Select("select count(*) as makecount from tb_makes where logsid in\n" +
            "(select id from tb_role_logs where userID = #{userid} and DATE_FORMAT(SYSDATE()- INTERVAL 1 DAY,'%Y-%m-%d') = DATE_FORMAT(cTime,'%Y-%m-%d') )")
    int selectMakecountByyesterday(Long userid);

    @Select("select count(*) from tb_role_logs where dataID = #{dataID}")
    int getTOtalByDataID(String dataID);

    @Select("select max(c.cTime) as cTime ,max(dataName) as dataName from tb_role_logs c where userID = #{userid} and dataID is not null GROUP BY dataID order by cTime desc limit 10")
    List<Map<String, String> >getHistoryByuserID(Long userid);

    @Select("select count(*) as countW,sum(if(liked = 'Y',1,0)) as countL from tb_role_logs where dataID = #{dataID} GROUP BY dataID")
    Map<String,Integer> serchLikedByDataID(String dataID);

    @Select("select count(*) from tb_role_logs where dataID = #{dataID} and liked = 'Y'")
    int getTotalLikedByDataID(String dataID);

    List<CaseReferrer> getCaseReferrerResultList(int provlimit);

    /**
     * 浏览统计后台
     *
     * @param page  page
     * @param param param
     * @return list
     */
    Page<Map<String, Object>> getRoleLogs(Page page, @Param(value = "param") GetRoleLogsParam param);

    /**
     * 浏览统计后台
     *
     * @param page  page
     * @return list
     */
    Page<Map<String, Object>> getDataResource(Page page);


    /**
     * 访问明细
     *
     * @param page  page
     * @param param param
     * @return list
     */
    Page<Map<String, Object>> visitingDetail(Page page, @Param(value = "param") VisitingDetailParam param);

    /**
     * 终端设备用户行为列表
     *
     * @param page  page
     * @param param param
     * @return list
     */
    Page<Map<String, Object>> getRolesLogList(Page page, @Param(value = "param") RoleLogsSeachList param);

    /**
     * 登录场景记录
     *
     * @param page  page
     * @param param param
     * @return list
     */
    Page<Map<String, Object>> getLoginViewRecords(Page page, @Param(value = "param") RoleLogsSearch param);

    /**
     * 场景内操作记录
     *
     * @param page  page
     * @param param param
     * @return list
     */
    Page<Map<String, Object>> getOperateRecords(Page page, @Param(value = "param") RoleLogsSearch param);

    /**
     * 查看评论
     *
     * @param page  page
     * @param param param
     * @return list
     */
    Page<Map<String, Object>> viewComments(Page page, @Param(value = "param") ViewCommentsParam param);

    /**
     * 评论详细
     *
     * @param param param
     * @return list
     */
    List<Map<String, Object>> commentsDetail(@Param(value = "param") CommentsDetailParam param);
}
