package org.zznode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zznode.dao.TbWorkflowConfigMapper;
import org.zznode.dto.FlowCenterListParam;
import org.zznode.entity.TbUsers;
import org.zznode.entity.TbWorkflowConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author frank
 * @date 2021/11/9
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TbWorkflowConfigService {

    private final TbWorkflowConfigMapper tbWorkflowConfigMapper;

    /**
     * 根据id获取信息
     *
     * @param id 主键id
     * @return 详细信息
     */
    public Map<String, Object> getById(long id) {
        Map<String, Object> result = new HashMap<>();
        // 基本信息
        TbWorkflowConfig tbWorkflowConfig = tbWorkflowConfigMapper.selectById(id);
        result.put("info", tbWorkflowConfig);
        return result;
    }

    /**
     * 流程配置
     *
     * @param workflowNo    workflowNo
     * @param workflowOrder workflowOrder
     * @return entity
     */
    public TbWorkflowConfig getWorkFlowConfigByWorkflowNoAndWorkflowOrder(Integer workflowNo,Integer workflowOrder){
        return tbWorkflowConfigMapper.getWorkFlowConfigByWorkflowNoAndWorkflowOrder(workflowNo,workflowOrder);
    }

}
