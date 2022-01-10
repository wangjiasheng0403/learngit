package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.zznode.dto.TbUserSearch;
import org.zznode.entity.TbUsers;

import java.util.List;
import java.util.Map;

/**
 * @description 针对表【tb_users(用户表)】的数据库操作Mapper
 * @Entity generator.domain.TbUsers
 */
public interface TbUsersMapper extends BaseMapper<TbUsers> {
    /**
     * 分页列表查询
     */
    @MapKey("UserID")
    Page<Map<String, Object>> getPageList(Page page, @Param("param") TbUserSearch param);

    /**
     * 通过主键查询明细
     */
    Map<String, String> selectInfoById(@Param("userId") Long userId);

    /**
     * 获取登录人所在组织信息
     */
    /**
     * 通过主键查询明细
     */
    @MapKey("UserID")
    Map<String, String> getParyByuserId(@Param("userId") Long userId);

    /**
     * 删除用户
     */
    void deleteUser(@Param("userId") Long userId);

    /**
     * 用户名和所在机构名
     *
     * @param userId userId
     * @return map
     */
    Map<String, String> getUserNameAndDepartmentName(@Param(value = "userId") Long userId);

    /**
     * 获取组织机构审批人
     *
     * @param orgId   组织id
     * @param rolesId 角色id
     * @return list
     */
    List<Map<Long, String>> getOrgApprover(@Param(value = "orgId") Long orgId, @Param(value = "rolesId") Long rolesId);

    /**
     * 获取行政机构审批人
     *
     * @param departmentId 部门id
     * @param rolesId      角色id
     * @return list
     */
    List<Map<Long, String>> getDepartmentApprover(@Param(value = "departmentId") Long departmentId, @Param(value = "rolesId") Long rolesId);


    /**
     * 用户名，机构名，党组织名
     *
     * @param userId userId
     * @return map
     */
    Map<String, String> getUserOrgNameByUserId(@Param(value = "userId") Long userId);
}




