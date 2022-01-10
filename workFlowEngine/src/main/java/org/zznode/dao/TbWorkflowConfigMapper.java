package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zznode.entity.TbWorkflowConfig;

import java.util.List;

/**
 * @description 针对表【tb_workflow_config(流程配置表)】的数据库操作Mapper
 * @Entity generator.domain.TbWorkflowConfig
 */
public interface TbWorkflowConfigMapper extends BaseMapper<TbWorkflowConfig> {
    /**
     * 流程配置
     *
     * @param workflowNo    workflowNo
     * @param workflowOrder workflowOrder
     * @return entity
     */
    @Select("select * from tb_workflow_config where workflowNo = #{workflowNo} and workflowOrder = #{workflowOrder}")
    TbWorkflowConfig getWorkFlowConfigByWorkflowNoAndWorkflowOrder(@Param(value = "workflowNo") Integer workflowNo, @Param(value = "workflowOrder") Integer workflowOrder);

    /**
     * 流程配置
     *
     * @param workflowNo workflowNo
     * @return list
     */
    @Select("select * from tb_workflow_config where workflowNo = #{workflowNo}")
    List<TbWorkflowConfig> getWorkFlowConfigByWorkflowNo(@Param(value = "workflowNo") Integer workflowNo);
}




