package org.zznode.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.zznode.dao.TbDepartmentMapper;
import org.zznode.dao.TbParyMapper;
import org.zznode.dao.TbUsersMapper;
import org.zznode.entity.TbDepartment;
import org.zznode.entity.TbPary;
import org.zznode.entity.TbUsers;
import org.zznode.entity.TbWorkflowConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApproverService {

    private final TbUsersMapper userMapper;
    private final TbDepartmentMapper departmentMapper;
    private final TbParyMapper paryMapper;

    public Map<String, Object> getApprover(Long applicantId, TbWorkflowConfig workflowConfig) {
        //党组织审批人
        if (workflowConfig.getOrgtype().equals(1)) {  //3  101 1
            return getOrgApprover(applicantId, workflowConfig.getRolesid(), workflowConfig.getUpperlevel());
        } else {
            return getDepartmentApprover(applicantId, workflowConfig.getRolesid(), workflowConfig.getUpperlevel());
        }
    }

    /**
     * 党组织审批人
     * 当前未找到获取上一级组织审批人一直到 lv = 1级别
     *
     * @param applicantId 申请人id
     * @param roseId      角色id
     * @param upperLevel  是否找上一级
     * @return list
     *///3  101 1
    private Map<String, Object> getOrgApprover(Long applicantId, Long roseId, Integer upperLevel) {
        Map<String, Object> result = new HashMap<>(2);
        //申请人信息
        TbUsers tbUsers = userMapper.selectById(applicantId);

        //申请人党组织信息
        TbPary pary = paryMapper.getParyById(tbUsers.getParyid());
        if (pary.getTreelv() != 1 && upperLevel.equals(1)) {
            pary = paryMapper.getParyById(pary.getFatherid());
        }

        List<Map<Long, String>> approverList = new ArrayList<>();

        boolean run = true;
        while (run) {
            //记录当前党组织级别
            // Chris 2021-12-08 18:22 增加pary非空判断
            if (!ObjectUtils.isEmpty(pary)) {

                result.put("paryLv", pary.getReallv());
                approverList = userMapper.getOrgApprover(pary.getPartid(), roseId);
                //已获取到记录或已到最上级组织不在查询
                if (approverList.size() > 0 || pary.getReallv().equals("1")) {
                    run = false;
                } else {
                    //取得上一级党组织信息
                    pary = paryMapper.getParyById(pary.getFatherid());
                }
            }
        }
        result.put("approver", approverList);
        return result;
    }

    /**
     * 行政机构审批人
     * 当前未找到获取上一级组织审批人一直到 lv = 1级别
     *
     * @param applicantId 申请人id
     * @param roseId      角色id
     * @param upperLevel  是否找上一级
     * @return list
     */
    private Map<String, Object> getDepartmentApprover(Long applicantId, Long roseId, Integer upperLevel) {
        Map<String, Object> result = new HashMap<>(2);
        //申请人信息
        TbUsers tbUsers = userMapper.selectById(applicantId);

        //申请人部门信息
        TbDepartment department = departmentMapper.selectById(tbUsers.getDepartid());
        if (upperLevel.equals(1)) {
            department = departmentMapper.selectById(department.getFatherid());
        }

        List<Map<Long, String>> approverList = new ArrayList<>();

        boolean run = true;
        while (run) {
            //记录当前党组织级别
            result.put("departmentLv", department.getLv());
            approverList = userMapper.getDepartmentApprover(department.getDepartid(), roseId);
            //已获取到记录或已到最上级组织不在查询
            if (approverList.size() > 0 || department.getLv() == 1) {
                run = false;
            } else {
                //取得上一级党组织信息
                department = departmentMapper.selectById(department.getFatherid());
            }
        }
        result.put("approver", approverList);
        return result;
    }
}
