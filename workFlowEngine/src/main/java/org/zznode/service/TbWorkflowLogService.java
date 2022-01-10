package org.zznode.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zznode.dao.TbWorkflowInfoMapper;
import org.zznode.dao.TbWorkflowLogMapper;
import org.zznode.entity.TbWorkflowInfo;
import org.zznode.entity.TbWorkflowLog;

/**
 * @author frank
 * @date 2021/11/9
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TbWorkflowLogService {

    private final TbWorkflowLogMapper tbWorkflowLogMapper;

    /**
     * 保存流程申请明细记录
     */
    @Transactional(rollbackFor = Exception.class)
    public int save(TbWorkflowLog tbWorkflowLog) {
       return tbWorkflowLogMapper.insert(tbWorkflowLog);
    }

}
