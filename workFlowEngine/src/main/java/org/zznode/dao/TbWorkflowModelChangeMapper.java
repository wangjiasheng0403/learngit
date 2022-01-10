package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.zznode.entity.TbWorkflowModelChange;

import java.util.List;
import java.util.Map;

/**
 * @description 针对表【tb_workflow_model_change(模型修改工作流明细表)】的数据库操作Mapper
 * @Entity generator.domain.TbWorkflowModelChange
 */
public interface TbWorkflowModelChangeMapper extends BaseMapper<TbWorkflowModelChange> {
    /**
     * 取得流程下所有元素数据
     *
     * @param modelChangeId modelChangeId
     * @return list
     */
    List<Map<String, Object>> getAppendixByInfoId(@Param(value = "modelChangeId") Long modelChangeId);

    /**
     * 当前应用的模型信息id
     *
     * @param dataId dataId
     * @return long
     */
    Long getLastCompletedModelChangeIdByDataId(@Param("dataId") String dataId);

    TbWorkflowModelChange getByWorkflowInfoId(@Param("workflowInfoId") Long workflowInfoId);

    List<Map<String, Object>> getPicture(@Param(value = "modelChangeId") Long modelChangeId);
}




