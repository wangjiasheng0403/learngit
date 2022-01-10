package org.zznode.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zznode.dao.TbWorkflowConfigMapper;
import org.zznode.dao.TbWorkflowInfoMapper;
import org.zznode.entity.TbWorkflowConfig;
import org.zznode.entity.TbWorkflowInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author frank
 * @date 2021/11/9
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TbWorkflowInfoService {

    private final TbWorkflowInfoMapper tbWorkflowInfoMapper;

    /**
     * 保存流程申请
     */
    @Transactional(rollbackFor = Exception.class)
    public int save(TbWorkflowInfo tbWorkflowInfo) {
       return tbWorkflowInfoMapper.insert(tbWorkflowInfo);
    }

}
