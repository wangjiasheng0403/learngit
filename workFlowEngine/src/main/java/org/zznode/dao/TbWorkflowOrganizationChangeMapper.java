package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zznode.entity.TbWorkflowOrganizationChange;

import java.util.Map;

/**
 * @description 针对表【tb_workflow_organization_change(组织架构修改工作流明细表)】的数据库操作Mapper
 * @Entity generator.domain.TbWorkflowOrganizationChange
 */
public interface TbWorkflowOrganizationChangeMapper extends BaseMapper<TbWorkflowOrganizationChange> {
    Map<String, Object> getByWorkflowId(@Param("workflowInfoId") long workflowInfoId);

    @Select("delete from tb_workflow_organization_change where workflowInfoId = #{workflowInfoId}")
    void deleteSaveDataByWorkflowInfoId(@Param(value = "workflowInfoId") Long workflowInfoId);
}




