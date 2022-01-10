package org.zznode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.zznode.entity.TbWorkflowLog;

import java.util.List;
import java.util.Map;

/**
 * @description 针对表【tb_workflow_log(流程记录表)】的数据库操作Mapper
 * @Entity generator.domain.TbWorkflowLog
 */
public interface TbWorkflowLogMapper extends BaseMapper<TbWorkflowLog> {

    @MapKey("id")
    List<Map<String, Object>> getLogListByWorkflowInfoId(@Param("workflowInfoId") long workflowInfoId, @Param("currentWorkflowOrder") int currentWorkflowOrder);

    TbWorkflowLog getLastByWorkflowInfoIdAndWorkflowOrder(@Param("workflowInfoId") long workflowInfoId, @Param("workflowOrder") int workflowOrder);
}




