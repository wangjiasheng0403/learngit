package org.zznode.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zznode.common.CustomException;
import org.zznode.dao.*;
import org.zznode.dto.FlowCenterListParam;
import org.zznode.dto.WorkflowApprovalParam;
import org.zznode.dto.WorkflowInfoParam;
import org.zznode.dto.WorkflowRecommitParam;
import org.zznode.entity.*;
import org.zznode.util.SnowflakeIdGenerator;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FlowCenterService {

    private final TbWorkflowInfoMapper tbWorkflowInfoMapper;
    private final TbWorkflowLogMapper tbWorkflowLogMapper;
    private final TbWorkflowConfigMapper tbWorkflowConfigMapper;
    private final TbWorkflowOperationAccountChangeMapper tbWorkflowOperationAccountChangeMapper;
    private final ScenesFlowService scenesFlowService;
    private final TbWorkflowModelChangeMapper tbWorkflowModelChangeMapper;
    private final TbWorkflowOrganizationChangeMapper tbWorkflowOrganizationChangeMapper;
    private final TbUsersMapper tbUsersMapper;
    private final TbParyMapper tbParyMapper;
    private final TbDataResourceMapper tbDataResourceMapper;
    private final ApproverService approverService;
    private final TbChangeModelMapper tbChangeModelMapper;
    private final TokenService tokenService;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    /**
     * 流程数据（待办）分页列表
     *
     * @param param 请求参数
     * @return 分页列表数据
     */
    public Page<Map<String, Object>> getPageListForBacklog(FlowCenterListParam param) {
        long userId = tokenService.getUserId(param.getToken());
        return tbWorkflowInfoMapper.getPageListForBacklog(new Page<>(param.getPageNo(), param.getPageSize()), userId);
    }

    /**
     * 流程数据（已办）分页列表
     *
     * @param param 请求参数
     * @return 分页列表数据
     */
    public Page<Map<String, Object>> getPageListForDone(FlowCenterListParam param) {
        long userId = tokenService.getUserId(param.getToken());
        return tbWorkflowInfoMapper.getPageListForDone(new Page<>(param.getPageNo(), param.getPageSize()), userId);
    }

    /**
     * 流程数据（发起）分页列表
     *
     * @param param 请求参数
     * @return 分页列表数据
     */
    public Page<Map<String, Object>> getPageListForOriginate(FlowCenterListParam param) {
        long userId = tokenService.getUserId(param.getToken());
        return tbWorkflowInfoMapper.getPageListForOriginate(new Page<>(param.getPageNo(), param.getPageSize()), userId);
    }

    /**
     * 流程数据（完成）分页列表
     *
     * @param param 请求参数
     * @return 分页列表数据
     */
    public Page<Map<String, Object>> getPageListForFinished(FlowCenterListParam param) {
        long userId = tokenService.getUserId(param.getToken());
        return tbWorkflowInfoMapper.getPageListForFinished(new Page<>(param.getPageNo(), param.getPageSize()), userId);
    }

    /**
     * 模型
     *
     * @param param 请求参数
     * @return 返回流程详细
     */
    public Map<String, Object> getModelInfo(WorkflowInfoParam param) {
        Map<String, Object> result = new HashMap<>(5);
        long userId = tokenService.getUserId(param.getToken());
        // 流程基本信息
        Map<String, Object> queryInfo = tbWorkflowInfoMapper.getWorkflowInfoById(param.getWorkflowInfoId());
        if ((int) queryInfo.get("workflowType") != 1) {
            throw new CustomException("000001", "类型错误", "流程类型错误：" + (int) queryInfo.get("workflowType"));
        }

        // 当前节点序号
        int currentWorkflowOrder = (int) queryInfo.get("currentWorkflowOrder");

        TbWorkflowModelChange queryWorkflowModelChange = tbWorkflowModelChangeMapper.getByWorkflowInfoId(param.getWorkflowInfoId());

        queryInfo.put("dataPath", queryWorkflowModelChange.getDatapath());
        queryInfo.put("type", queryWorkflowModelChange.getType());
        queryInfo.put("dataName", queryWorkflowModelChange.getDataname());
        // 基本信息
        result.put("info", queryInfo);
        // 场景明细
        result.put("detail", scenesFlowService.getScenesFlowDataByInfoId(queryWorkflowModelChange.getId()).get("list"));

        List<Map<String, Object>> logList = tbWorkflowLogMapper.getLogListByWorkflowInfoId(param.getWorkflowInfoId(), (int) queryInfo.get("currentWorkflowOrder"));
        result.put("logList", logList);

        // 判断是否有下一个审核节点 99 为最后一个节点
        if (currentWorkflowOrder != 99) {
            TbWorkflowConfig nextConfig = this.nextWorkflowConfig(1001L, currentWorkflowOrder);

            // 如果下一个节点是99，审核人为申请人
            if (nextConfig.getWorkfloworder() == 99) {
                List<Map<String, Object>> list = new ArrayList<>();
                Map<String, Object> map = new HashMap<>(2);
                map.put("userId", queryInfo.get("userID"));
                map.put("userName", queryInfo.get("userName"));
                list.add(map);
                result.put("approver", list);
            } else {
                Map<String, Object> a = approverService.getApprover(userId, nextConfig);
                if (a != null) {
                    result.put("approver", a.get("approver"));
                } else {
                    result.put("approver", null);
                }
            }
        }
        return result;
    }

    /**
     * 内容运营
     *
     * @param param 请求参数
     * @return 返回流程详细
     */
    public Map<String, Object> getOperationAccountInfo(WorkflowInfoParam param) {
        Map<String, Object> result = new HashMap<>(5);
        long userId = tokenService.getUserId(param.getToken());
        // 流程基本信息
        Map<String, Object> queryInfo = tbWorkflowInfoMapper.getWorkflowInfoById(param.getWorkflowInfoId());
        if ((int) queryInfo.get("workflowType") != 2) {
            throw new CustomException("000001", "类型错误", "流程类型错误：" + (int) queryInfo.get("workflowType"));
        }

        // 当前节点序号
        int currentWorkflowOrder = (int) queryInfo.get("currentWorkflowOrder");

        result.put("detail", tbWorkflowOperationAccountChangeMapper.getByWorkflowInfoId(param.getWorkflowInfoId()));

        // 基本信息
        result.put("info", queryInfo);

        List<Map<String, Object>> logList = tbWorkflowLogMapper.getLogListByWorkflowInfoId(param.getWorkflowInfoId(), (int) queryInfo.get("currentWorkflowOrder"));
        result.put("logList", logList);

        // 判断是否有下一个审核节点 99 为最后一个节点
        if (currentWorkflowOrder != 99) {
            TbWorkflowConfig nextConfig = this.nextWorkflowConfig(1002L, currentWorkflowOrder);
            Map<String, Object> a = approverService.getApprover(userId, nextConfig);
            if (a != null) {
                result.put("approver", a.get("approver"));
            } else {
                result.put("approver", null);
            }
        }
        return result;
    }

    /**
     * 组织
     *
     * @param param 请求参数
     * @return 返回流程详细
     */
    public Map<String, Object> getOrganizationInfo(WorkflowInfoParam param) {
        Map<String, Object> result = new HashMap<>(5);
        long userId = tokenService.getUserId(param.getToken());
        // 流程基本信息
        Map<String, Object> queryInfo = tbWorkflowInfoMapper.getWorkflowInfoById(param.getWorkflowInfoId());
        if ((int) queryInfo.get("workflowType") != 3) {
            throw new CustomException("000001", "类型错误", "流程类型错误：" + (int) queryInfo.get("workflowType"));
        }

        // 当前节点序号
        int currentWorkflowOrder = (int) queryInfo.get("currentWorkflowOrder");

        result.put("detail", tbWorkflowOrganizationChangeMapper.getByWorkflowId(param.getWorkflowInfoId()));

        // 基本信息
        result.put("info", queryInfo);

        List<Map<String, Object>> logList = tbWorkflowLogMapper.getLogListByWorkflowInfoId(param.getWorkflowInfoId(), (int) queryInfo.get("currentWorkflowOrder"));
        result.put("logList", logList);

        System.out.println("result:" + result);

        // 判断是否有下一个审核节点 99 为最后一个节点
        if (currentWorkflowOrder != 99) {

            TbWorkflowConfig nextConfig = this.nextWorkflowConfig(1003L, currentWorkflowOrder);
            // 如果下一个节点是99，审核人为申请人
            if (nextConfig.getWorkfloworder() == 99) {
                List<Map<String, Object>> list = new ArrayList<>();
                Map<String, Object> map = new HashMap<>(2);
                map.put("userId", queryInfo.get("userID"));
                map.put("userName", queryInfo.get("userName"));
                list.add(map);
                result.put("approver", list);
            } else {
                Map<String, Object> a = approverService.getApprover(userId, nextConfig);
                if (a != null) {
                    result.put("approver", a.get("approver"));
                } else {
                    result.put("approver", null);
                }
            }
        }
        System.out.println("result:" + result);
        return result;
    }

    /**
     * 流程审批
     *
     * @param param 请求参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void approval(WorkflowApprovalParam param) {
        long userId = tokenService.getUserId(param.getToken());

        // 工作流信息
        TbWorkflowInfo queryInfo = tbWorkflowInfoMapper.selectById(param.getWorkflowInfoId());

        // 审核节点
        TbWorkflowLog queryLog = tbWorkflowLogMapper.selectById(param.getWorkflowLogId());

        if (queryInfo == null || queryLog == null) {
            throw new CustomException("000001", "数据错误", "没有要审核的流程");
        }

        if (queryLog.getStatus() != 0) {
            throw new CustomException("000006", "不能重复审批", "该节点已经审批，无需再次审批");
        }

        if (!queryLog.getUserid().equals(userId)) {
            throw new CustomException("000007", "审批错误","审核人和登陆用户不符，申请刷新重试");
        }

        // 流程编号
        int workflowNo = queryInfo.getWorkflowno();
        // 当前审核节点序号
        int currentWorkflowOrder = queryLog.getWorkfloworder();

        // 更新流程状态
        queryLog.setStatus(param.getOpStatus());
        queryLog.setOptime((new Date()));
        queryLog.setMake(param.getMake());
        tbWorkflowLogMapper.updateById(queryLog);

        // 判断审核状态
        if (param.getOpStatus() == 1) {
            // opStatus == 1 通过
            // 判断当前节点是不是99，99代表流程已经走完
            if (currentWorkflowOrder == 99) {
                // 如果是99 工作流结束
                queryInfo.setStatus(2);
                tbWorkflowInfoMapper.updateById(queryInfo);
                // 插入业务表数据
                if (queryInfo.getWorkflowtype() == 1) {
                    // 1模型修改 (model_change)
                    TbWorkflowModelChange queryTbWorkflowModelChange = tbWorkflowModelChangeMapper.selectOne(new QueryWrapper<TbWorkflowModelChange>().lambda()
                            .eq(TbWorkflowModelChange::getWorkflowinfoid, param.getWorkflowInfoId()));

                    //  如果 是新建模型
                    if (queryTbWorkflowModelChange.getType() == 1) {
                        //TbDataResource queryDataResource = tbDataResourceMapper.selectById(queryTbWorkflowModelChange.getDataid());
                        //queryDataResource.setDataname(queryTbWorkflowModelChange.getDataname());
                        //tbDataResourceMapper.updateById(queryDataResource);
                    }
                } else if (queryInfo.getWorkflowtype() == 2) {
                    // 2运营方账号修改 (operation_account_change)
                    TbWorkflowOperationAccountChange queryTbWorkflowOperationAccountChange = tbWorkflowOperationAccountChangeMapper.selectOne(new QueryWrapper<TbWorkflowOperationAccountChange>().lambda()
                            .eq(TbWorkflowOperationAccountChange::getWorkflowinfoid, param.getWorkflowInfoId()));
                    Long operateuserid = queryTbWorkflowOperationAccountChange.getOperateuserid();
                    tbUsersMapper.deleteUser(operateuserid);
                } else if (queryInfo.getWorkflowtype() == 3) {
                    // 3组织机构管理
                    TbWorkflowOrganizationChange queryTbWorkflowOperationChange = tbWorkflowOrganizationChangeMapper.selectOne(new QueryWrapper<TbWorkflowOrganizationChange>().lambda()
                            .eq(TbWorkflowOrganizationChange::getWorkflowinfoid, param.getWorkflowInfoId()));
                    // 判断流程类型
                    if (queryTbWorkflowOperationChange.getType() == 1 || queryTbWorkflowOperationChange.getType() == 2) {
                        // 1增加 或 2修改
                        // 找上级部门
                        TbPary queryTbPary = tbParyMapper.selectOne(new QueryWrapper<TbPary>().lambda()
                                .eq(TbPary::getPartid, queryTbWorkflowOperationChange.getParentdepartid()));
                        int treeLv = 1, realLv = 1;
                        if (queryTbPary != null) {
                            treeLv = queryTbPary.getTreelv() + 1;
                            realLv = Integer.parseInt(queryTbPary.getReallv()) + 1;
                        }
                        TbPary tbPary;
                        if (queryTbWorkflowOperationChange.getType() == 1) {
                            tbPary = new TbPary();
                            tbPary.setPartid(snowflakeIdGenerator.nextId());
                            tbPary.setCreatetime(new Date());
                        } else {
                            tbPary = tbParyMapper.selectById(queryTbWorkflowOperationChange.getOperationdepartid());
                        }
                        tbPary.setFatherid(queryTbWorkflowOperationChange.getParentdepartid());
                        tbPary.setPartname(queryTbWorkflowOperationChange.getDepartname());
                        tbPary.setTreelv(treeLv);
                        tbPary.setReallv(realLv + "");
                        tbPary.setProvepath(queryTbWorkflowOperationChange.getAttachment());
                        if (queryTbWorkflowOperationChange.getType() == 1) {
                            tbParyMapper.insert(tbPary);
                        } else {
                            tbParyMapper.updateById(tbPary);
                        }
                    } else if (queryTbWorkflowOperationChange.getType() == 3) {
                        // 删除
                        tbParyMapper.deletePary(queryTbWorkflowOperationChange.getOperationdepartid());
                    } else {
                        throw new CustomException("000002", "数据错误", "无效的操作类型[1/2/3]：" + queryTbWorkflowOperationChange.getType());
                    }
                } else {
                    throw new CustomException("000003", "数据错误", "无效的流程类型[1/2/3]：" + queryInfo.getWorkflowtype());
                }

            } else {
                if (param.getApproverId() == null) {
                    throw new CustomException("000004", "审核人不能为空", "不是最后一个审核节点，必须指定一个审核人");
                }
                // 不是最后一个节点，生成下一个节点数据
                // 获取下一个流程节点
                TbWorkflowConfig nextConfig = this.nextWorkflowConfig(workflowNo, currentWorkflowOrder);

                // 更新流程表
                queryInfo.setCurrentworkfloworder(nextConfig.getWorkfloworder());
                tbWorkflowInfoMapper.updateById(queryInfo);

                // 下一个审核节点数据
                TbWorkflowLog nextWorkflowLog = new TbWorkflowLog();
                nextWorkflowLog.setId(snowflakeIdGenerator.nextId());
                nextWorkflowLog.setWorkflowno(workflowNo);
                nextWorkflowLog.setWorkflowinfoid(param.getWorkflowInfoId());
                nextWorkflowLog.setStatus(0);
                nextWorkflowLog.setWorkfloworder(nextConfig.getWorkfloworder());
                TbUsers queryUser = tbUsersMapper.selectById(param.getApproverId());
                nextWorkflowLog.setUserid(param.getApproverId());
                nextWorkflowLog.setDepartid(queryUser.getDepartid());

                tbWorkflowLogMapper.insert(nextWorkflowLog);
            }
        } else if (param.getOpStatus() == 2) {
            // opStatus == 2 拒绝
            // 当前审核节点配置信息
            TbWorkflowConfig queryConfig = tbWorkflowConfigMapper.selectOne(new QueryWrapper<TbWorkflowConfig>().lambda()
                    .eq(TbWorkflowConfig::getWorkflowno, workflowNo)
                    .eq(TbWorkflowConfig::getWorkfloworder, currentWorkflowOrder));

            // 审核节点回退到上一个节点
            queryInfo.setCurrentworkfloworder(queryConfig.getBackworkfloworder());
            tbWorkflowInfoMapper.updateById(queryInfo);

            // 创建上一节点数据
            // 查询回退节点的审核信息
            TbWorkflowLog tbWorkflowLog = tbWorkflowLogMapper.getLastByWorkflowInfoIdAndWorkflowOrder(param.getWorkflowInfoId(), queryConfig.getBackworkfloworder());
            // 清空必要数据
            tbWorkflowLog.setId(snowflakeIdGenerator.nextId());
            tbWorkflowLog.setStatus(0);
            tbWorkflowLog.setMake("");
            tbWorkflowLog.setOptime(null);
            // 新建回退节点
            tbWorkflowLogMapper.insert(tbWorkflowLog);
        } else {
            throw new CustomException("000005", "数据错误", "无效的审核状态[1/2]：" + param.getOpStatus());
        }

    }

    /**
     * 重新提交申请
     */
    @Transactional(rollbackFor = Exception.class)
    public void modelRecommit(WorkflowRecommitParam param) {
        // 流程基本信息
        TbWorkflowInfo queryWorkflowInfo = tbWorkflowInfoMapper.selectById(param.getWorkflowInfoId());

        // 当前节点序号
        int currentWorkflowOrder = queryWorkflowInfo.getCurrentworkfloworder();

        // 下一个审核节点信息
        TbWorkflowConfig nextConfig = this.nextWorkflowConfig(1001, currentWorkflowOrder);

        // 更新流程表
        queryWorkflowInfo.setCurrentworkfloworder(nextConfig.getWorkfloworder());
        tbWorkflowInfoMapper.updateById(queryWorkflowInfo);

        // model_chagne信息
        TbWorkflowModelChange queryWorkflowModelChange = tbWorkflowModelChangeMapper.getByWorkflowInfoId(param.getWorkflowInfoId());
        queryWorkflowModelChange.setDataname(param.getDataName());
        queryWorkflowModelChange.setAppendixpath(param.getAttachment());
        // 更新model_change信息
        tbWorkflowModelChangeMapper.updateById(queryWorkflowModelChange);

        // 修改change_model 信息
        for (WorkflowRecommitParam.ModelChange modelChange : param.getModelChangeList()) {
            TbChangeModel queryChangeModel = tbChangeModelMapper.selectById(modelChange.getId());
            queryChangeModel.setMatepath(modelChange.getMatePath());
            tbChangeModelMapper.updateById(queryChangeModel);
        }

        // 下一个审核节点数据
        TbWorkflowLog nextWorkflowLog = new TbWorkflowLog();
        nextWorkflowLog.setId(snowflakeIdGenerator.nextId());
        nextWorkflowLog.setWorkflowno(queryWorkflowInfo.getWorkflowno());
        nextWorkflowLog.setWorkflowinfoid(param.getWorkflowInfoId());
        nextWorkflowLog.setStatus(0);
        nextWorkflowLog.setWorkfloworder(nextConfig.getWorkfloworder());
        TbUsers queryUser = tbUsersMapper.selectById(param.getApproverId());
        nextWorkflowLog.setUserid(param.getApproverId());
        nextWorkflowLog.setDepartid(queryUser.getDepartid());
        // 插入下一个节点数据
        tbWorkflowLogMapper.insert(nextWorkflowLog);

        // 更新提交人审核意见
        TbWorkflowLog queryWorkflowLog = tbWorkflowLogMapper.selectById(param.getWorkflowLogId());
        queryWorkflowLog.setMake(param.getMake());
        queryWorkflowLog.setOptime(new Date());
        queryWorkflowLog.setStatus(1);
        tbWorkflowLogMapper.updateById(queryWorkflowLog);
    }

    /**
     * 获取下一个节点信息
     *
     * @param workflowNo           工作流编号 1001 模型修改 1002 运营账号修改 1003 组织架构修改
     * @param currentWorkflowOrder 当前节点需要
     * @return 下一个节点信息
     */
    private TbWorkflowConfig nextWorkflowConfig(long workflowNo, int currentWorkflowOrder) {
        // 整个流程配置信息
        List<TbWorkflowConfig> configList = tbWorkflowConfigMapper.selectList(new QueryWrapper<TbWorkflowConfig>().lambda()
                .eq(TbWorkflowConfig::getWorkflowno, workflowNo));
        System.out.println("1configList：" + configList);
        // 按序号排序
        configList = configList.stream().sorted(Comparator.comparing(TbWorkflowConfig::getWorkfloworder)).collect(Collectors.toList());
        System.out.println("2configList：" + configList);
        // 获取流程节点
        AtomicInteger index = new AtomicInteger(0);
        for (TbWorkflowConfig config : configList) {
            if (config.getWorkfloworder().equals(currentWorkflowOrder)) {
                break;
            }
            index.getAndIncrement();
        }
        return configList.get(index.get() + 1);
    }
}

