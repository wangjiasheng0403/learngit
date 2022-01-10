package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.zznode.entity.TbWorkflowOperationAccountChange;

import java.util.Map;

/**
 * @description 针对表【tb_workflow_operation_account_change(运营账号修改工作流明细表)】的数据库操作Mapper
 * @Entity generator.domain.TbWorkflowOperationAccountChange
 */
public interface TbWorkflowOperationAccountChangeMapper extends BaseMapper<TbWorkflowOperationAccountChange> {
    Map<String, Object> getByWorkflowInfoId(@Param("workflowInfoId") long workflowInfoId);
    Map<String, Object> getSaveData(@Param("deleteUserId") long deleteUserId);
}




