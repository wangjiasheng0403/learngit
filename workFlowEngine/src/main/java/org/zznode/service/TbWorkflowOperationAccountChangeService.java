package org.zznode.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.zznode.dao.TbWorkflowOperationAccountChangeMapper;
import org.zznode.entity.TbRolesUsers;
import org.zznode.entity.TbWorkflowOperationAccountChange;
import org.zznode.util.SnowflakeIdGenerator;

import java.util.List;
import java.util.Map;

/**
 * @author frank
 * @date 2021/11/9
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TbWorkflowOperationAccountChangeService {
    private final TbWorkflowOperationAccountChangeMapper tbWorkflowOperationAccountChangeMapper;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    /**
     * 保存运营账号信息申请
     *
     * @param paramMap 运营账号参数
     */
    @Transactional(rollbackFor = Exception.class)
    public int save(Long workflowInfoId, Map<String, String> paramMap) {
        TbWorkflowOperationAccountChange entity = new TbWorkflowOperationAccountChange();
        entity.setId(snowflakeIdGenerator.nextId());
        if(!ObjectUtils.isEmpty(paramMap.get("UserID"))){
            entity.setOperateuserid(Long.parseLong(String.valueOf(paramMap.get("UserID"))));
        }else{
            entity.setOperateuserid(0L);
        }

        entity.setOperateaccount(paramMap.get("Account"));
        entity.setOperatepwd(paramMap.get("passWord"));
        if(!ObjectUtils.isEmpty(paramMap.get("partID"))){
            entity.setOperatedepartid(Long.parseLong(String.valueOf(paramMap.get("partID"))));
        }else{
            entity.setOperatedepartid(0L);
        }
        entity.setUsername(paramMap.get("UserName"));
        entity.setPhone(paramMap.get("Phone"));
        entity.setEmail(paramMap.get("Email"));
        entity.setSex(paramMap.get("Sex"));
        entity.setAccountstatus(paramMap.get("AccountStatus"));
        entity.setWorkflowinfoid(workflowInfoId);
        entity.setType(3);
        entity.setFilepath(paramMap.get("filePath"));
        entity.setDeletecontent(paramMap.get("deleteContent"));
        return tbWorkflowOperationAccountChangeMapper.insert(entity);
    }

    /**
     * 获取保存数据记录
     */
    public Map<String,Object> getSaveData(Long deleteUserId){
        return tbWorkflowOperationAccountChangeMapper.getSaveData(deleteUserId);
    }
}
