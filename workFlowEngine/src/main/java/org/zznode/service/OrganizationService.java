package org.zznode.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.zznode.common.CommonResult;
import org.zznode.common.CustomException;
import org.zznode.dao.*;
import org.zznode.dto.*;
import org.zznode.entity.*;
import org.zznode.util.SnowflakeIdGenerator;
import org.zznode.util.WorkFlowUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrganizationService {

    private final TbUsersMapper tbUsersMapper;
    private final ApproverService approverService;
    private final TbWorkflowConfigMapper tbWorkflowConfigMapper;
    private final TbParyMapper tbParyMapper;
    private final WorkFlowUtils workFlowUtils;
    private final TbWorkflowInfoMapper tbWorkflowInfoMapper;
    private final TbUsersMapper userMapper;
    private final TokenService tokenService;
    private final TbWorkflowOrganizationChangeMapper tbWorkflowOrganizationChangeMapper;
    private final TbWorkflowLogMapper tbWorkflowLogMapper;

    private static final String WORKFLOW_FIRST_TEXT = "ZZXJ";

    private static final String MANAGER = "管理员";
    private final SnowflakeIdGenerator snowflakeIdGenerator;

    /**
     * 初始化流程数据
     */
    public Map<String, Object> init(InitOrganizationParam param) {

        Long userId = tokenService.getUserId(param.getToken());

        Map<String, Object> result = Maps.newHashMap();

        Map<String, String> queryUser = userMapper.getUserOrgNameByUserId(userId);
        Map<String, Object> info = Maps.newHashMap();
        info.put("username", queryUser.get("userName"));
        info.put("workflowNo", workFlowUtils.nextCode(WORKFLOW_FIRST_TEXT));
        info.put("departName", queryUser.get("departName"));
        info.put("partName", queryUser.get("partName"));

        result.put("info", info);

        List<TbWorkflowConfig> queryWorkflowConfigList = tbWorkflowConfigMapper.selectList(new QueryWrapper<TbWorkflowConfig>().lambda()
                .eq(TbWorkflowConfig::getWorkflowno, 1003L));
        // 按序号排序
        queryWorkflowConfigList = queryWorkflowConfigList.stream().sorted(Comparator.comparing(TbWorkflowConfig::getWorkfloworder)).collect(Collectors.toList());

        // 第一个节点（index=0）为下标以为申请人，从第二个节(index=1)点开始
        TbWorkflowConfig tbWorkflowConfig = queryWorkflowConfigList.get(1);

        result.put("approver", approverService.getApprover(userId, tbWorkflowConfig).get("approver"));

        // 如果organizationId不为空，查询组织信息
        if (param.getOrganizationId() != null) {
            TbPary tbPary = tbParyMapper.selectById(param.getOrganizationId());
            result.put("detail", tbPary);
        }
        result.put("parentOrg", tbParyMapper.paryTree(2));

        TbWorkflowInfo workflowInfo = tbWorkflowInfoMapper.getSaveOrgWorkFlowInfo();
        if (workflowInfo != null) {
            Map<String, Object> operationAccountChange = tbWorkflowOrganizationChangeMapper.getByWorkflowId(workflowInfo.getId());
            result.put("saveData", operationAccountChange);
            result.put("workflowInfoId", workflowInfo.getId());
        }

        return result;
    }

    /**
     * 添加流程
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<?> add(AddOrganizationParam param) {
        TbUsers users = tokenService.getUser(param.getToken());
        TbUsers approverUser = tbUsersMapper.selectById(param.getApproverId());
        // 插入workflow info 数据
        TbWorkflowInfo tbWorkflowInfo = new TbWorkflowInfo();
        System.out.println("snowflakeIdGenerator:" + snowflakeIdGenerator.nextId());

        tbWorkflowInfo.setId(snowflakeIdGenerator.nextId());
        tbWorkflowInfo.setWorkflowno(1003);
        tbWorkflowInfo.setCurrentworkfloworder(2);
        tbWorkflowInfo.setStatus(param.getCommitStatus());
        tbWorkflowInfo.setWorkflowtype(3);
        tbWorkflowInfo.setWorkflowname("新增党组织机构" + param.getOrganizationName() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        tbWorkflowInfo.setOriginator(users.getUserid());
        tbWorkflowInfo.setCreatetime(new Date());
        tbWorkflowInfo.setOrderno(param.getWorkflowNo());
        tbWorkflowInfoMapper.insert(tbWorkflowInfo);
        // 插入流程change表数据
        TbWorkflowOrganizationChange tbWorkflowOrganizationChange = new TbWorkflowOrganizationChange();
        tbWorkflowOrganizationChange.setAttachment(param.getAttachment());
        tbWorkflowOrganizationChange.setWorkflowinfoid(tbWorkflowInfo.getId());
        tbWorkflowOrganizationChange.setId(snowflakeIdGenerator.nextId());
        tbWorkflowOrganizationChange.setDepartname(param.getOrganizationName());
        tbWorkflowOrganizationChange.setType(1);
        tbWorkflowOrganizationChange.setOperationdepartid(null);
        tbWorkflowOrganizationChange.setParentdepartid(param.getParentOrganizationId());
        tbWorkflowOrganizationChangeMapper.insert(tbWorkflowOrganizationChange);
        for (int i = 1; i <= 2; i++) {
            TbWorkflowLog workflowLog = new TbWorkflowLog();
            workflowLog.setId(snowflakeIdGenerator.nextId());
            if (i == 1) {
                // log 1 申请人
                workflowLog.setStatus(1);
                workflowLog.setUserid(users.getUserid());
                workflowLog.setDepartid(users.getDepartid());
            } else {
                // log 2 审核人
                workflowLog.setStatus(0);
                workflowLog.setUserid(param.getApproverId());
                workflowLog.setDepartid(approverUser.getDepartid());
            }
            workflowLog.setMake("");
            workflowLog.setOptime(new Date());
            workflowLog.setWorkflowno(1003);
            workflowLog.setWorkfloworder(i);
            workflowLog.setWorkflowinfoid(tbWorkflowInfo.getId());
            tbWorkflowLogMapper.insert(workflowLog);
        }
        emptySaveData(param.getWorkflowInfoId());
        return CommonResult.success();
    }


    /**
     * 修改流程
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<?> modify(ModifyOrganizationParam param) {
        TbUsers users = tokenService.getUser(param.getToken());
        TbUsers approverUser = tbUsersMapper.selectById(param.getApproverId());
        // 插入workflow info 数据
        TbWorkflowInfo tbWorkflowInfo = new TbWorkflowInfo();
        tbWorkflowInfo.setId(snowflakeIdGenerator.nextId());
        tbWorkflowInfo.setWorkflowno(1003);
        tbWorkflowInfo.setCurrentworkfloworder(2);
        tbWorkflowInfo.setStatus(param.getCommitStatus());
        tbWorkflowInfo.setWorkflowtype(3);
        tbWorkflowInfo.setWorkflowname("更新党组织机构" + param.getOrganizationName() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        tbWorkflowInfo.setOriginator(users.getUserid());
        tbWorkflowInfo.setCreatetime(new Date());
        tbWorkflowInfo.setOrderno(param.getWorkflowNo());
        tbWorkflowInfoMapper.insert(tbWorkflowInfo);
        // 插入流程change表数据
        TbWorkflowOrganizationChange tbWorkflowOrganizationChange = new TbWorkflowOrganizationChange();
        tbWorkflowOrganizationChange.setAttachment(param.getAttachment());
        tbWorkflowOrganizationChange.setWorkflowinfoid(tbWorkflowInfo.getId());
        tbWorkflowOrganizationChange.setId(snowflakeIdGenerator.nextId());
        tbWorkflowOrganizationChange.setDepartname(param.getOrganizationName());
        tbWorkflowOrganizationChange.setType(2);
        tbWorkflowOrganizationChange.setOperationdepartid(param.getPartID());
        tbWorkflowOrganizationChange.setParentdepartid(param.getParentOrganizationId());
        tbWorkflowOrganizationChangeMapper.insert(tbWorkflowOrganizationChange);
        for (int i = 1; i <= 2; i++) {
            TbWorkflowLog workflowLog = new TbWorkflowLog();
            workflowLog.setId(snowflakeIdGenerator.nextId());
            if (i == 1) {
                // log 1 申请人
                workflowLog.setStatus(1);
                workflowLog.setUserid(users.getUserid());
                workflowLog.setDepartid(users.getDepartid());
            } else {
                // log 2 审核人
                workflowLog.setStatus(0);
                workflowLog.setUserid(param.getApproverId());
                workflowLog.setDepartid(approverUser.getDepartid());
            }
            workflowLog.setMake("");
            workflowLog.setOptime(new Date());
            workflowLog.setWorkflowno(1003);
            workflowLog.setWorkfloworder(i);
            workflowLog.setWorkflowinfoid(tbWorkflowInfo.getId());
            tbWorkflowLogMapper.insert(workflowLog);
        }
        emptySaveData(param.getWorkflowInfoId());
        return CommonResult.success();
    }

    /**
     * 删除流程
     */
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<?> remove(RemoveOrganizationParam param) {
        Long userId = tokenService.getUserId(param.getToken());
        List<TbPary> tbParyList = tbParyMapper.selectList(new QueryWrapper<TbPary>().lambda().eq(TbPary::getFatherid, param.getOrganizationId()));
        if (CollectionUtils.isEmpty(tbParyList)) {
            TbPary tbPary = tbParyMapper.getParyById(param.getOrganizationId());
            // 插入workflow info 数据
            TbWorkflowInfo tbWorkflowInfo = new TbWorkflowInfo();
            tbWorkflowInfo.setId(snowflakeIdGenerator.nextId());
            tbWorkflowInfo.setWorkflowno(1003);
            tbWorkflowInfo.setCurrentworkfloworder(2);
            tbWorkflowInfo.setStatus(param.getCommitStatus());
            tbWorkflowInfo.setWorkflowtype(3);
            tbWorkflowInfo.setWorkflowname("删除党组织机构" + tbPary.getPartname() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            tbWorkflowInfo.setOriginator(userId);
            tbWorkflowInfo.setCreatetime(new Date());
            tbWorkflowInfo.setOrderno(param.getWorkflowNo());
            tbWorkflowInfoMapper.insert(tbWorkflowInfo);
            // 插入流程change表数据
            TbWorkflowOrganizationChange tbWorkflowOrganizationChange = new TbWorkflowOrganizationChange();
            tbWorkflowOrganizationChange.setAttachment(param.getAttachment());
            tbWorkflowOrganizationChange.setWorkflowinfoid(tbWorkflowInfo.getId());
            tbWorkflowOrganizationChange.setId(snowflakeIdGenerator.nextId());
            tbWorkflowOrganizationChange.setDepartname(tbPary.getPartname());
            tbWorkflowOrganizationChange.setType(3);
            tbWorkflowOrganizationChange.setOperationdepartid(param.getPartID());
            tbWorkflowOrganizationChange.setParentdepartid(tbPary.getPartid());
            tbWorkflowOrganizationChangeMapper.insert(tbWorkflowOrganizationChange);
            TbUsers users = tbUsersMapper.selectById(userId);
            TbUsers approverUser = tbUsersMapper.selectById(param.getApproverId());
            for (int i = 1; i <= 2; i++) {
                TbWorkflowLog workflowLog = new TbWorkflowLog();
                workflowLog.setId(snowflakeIdGenerator.nextId());
                if (i == 1) {
                    // log 1 申请人
                    workflowLog.setStatus(1);
                    workflowLog.setUserid(userId);
                    workflowLog.setDepartid(users.getDepartid());
                } else {
                    // log 2 审核人
                    workflowLog.setStatus(0);
                    workflowLog.setUserid(param.getApproverId());
                    workflowLog.setDepartid(approverUser.getDepartid());
                }
                workflowLog.setMake("");
                workflowLog.setOptime(new Date());
                workflowLog.setWorkflowno(1003);
                workflowLog.setWorkfloworder(i);
                workflowLog.setWorkflowinfoid(tbWorkflowInfo.getId());
                tbWorkflowLogMapper.insert(workflowLog);
            }
            emptySaveData(param.getWorkflowInfoId());
            return CommonResult.success();
        } else {
            throw new CustomException("100303", "对于其下有分支机或相关人员的组织结构，不允许删除", "");
        }
    }

    /**
     * 查询党组织机构列表
     *
     * @param param ListOrganizationParam
     * @return 党组织机构列表
     */
    public IPage<Map<String, Object>> list(ListOrganizationParam param) {
        Long userId = tokenService.getUserId(param.getToken());
        //Long userId = param.getUserId();
        param.setUserId(userId);
        List<Map<String, Object>> rolesList = tbParyMapper.getUserRoles(userId);
        boolean searchFlag = false;
        if (!CollectionUtils.isEmpty(rolesList)) {
            for (Map<String, Object> stringObjectMap : rolesList) {
                if (MANAGER.equals(stringObjectMap.get("rolesName").toString())) {
                    searchFlag = true;
                }
            }
        }
        IPage<Map<String, Object>> dataMap;
        IPage<TbPary> page = new Page<>(param.getPageNo(), param.getPageSize());
        if (searchFlag) {
            dataMap = tbParyMapper.getTbParyMaps(page, param);
        } else {
            dataMap = tbParyMapper.getTbPary(page, param);
        }
        dataMap.getRecords().forEach((item) -> {
            item.put("children", tbParyMapper.getParyFatherId(String.valueOf(item.get("partID"))));
        });
        return dataMap;
    }

    /**
     * 删除 用户保存的数据
     * 用户在已保存内容上修改删除原始记录
     *
     * @param workflowInfoId workflowInfoId
     */
    private void emptySaveData(Long workflowInfoId) {
        if (workflowInfoId != null) {
            tbWorkflowInfoMapper.deleteById(workflowInfoId);
            tbWorkflowOrganizationChangeMapper.deleteSaveDataByWorkflowInfoId(workflowInfoId);
        }
    }
}
