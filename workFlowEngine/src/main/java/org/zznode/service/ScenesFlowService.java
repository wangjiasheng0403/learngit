package org.zznode.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.zznode.dao.*;
import org.zznode.dto.CreateWorkflowParam;
import org.zznode.entity.*;
import org.zznode.util.SnowflakeIdGenerator;
import org.zznode.util.WorkFlowUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScenesFlowService {

    private final TbUsersMapper userMapper;
    private final TbDataResourceMapper dataResourceMapper;
    private final TbModelPictureMapper modelPictureMapper;
    private final TbModelPictureAppendixMapper modelPictureAppendixMapper;
    private final TbWorkflowModelChangeMapper workflowModelChangeMapper;
    private final TbWorkflowInfoMapper workflowInfoMapper;
    private final TbWorkflowLogMapper workflowLogMapper;
    private final ApproverService approverService;
    private final TbWorkflowConfigMapper workflowConfigMapper;
    private final TbChangeModelMapper changeModelMapper;
    private final WorkFlowUtils workFlowUtils;
    private final TokenService tokenService;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    @Resource
    private ScenesFlowService scenesFlowService;

    /**
     * 场景修改基本信息
     * 返回 流程编号， 用户名， 用户所属组织， 组织下场景
     *
     * @param userId userId
     * @return map
     */
    public Map<String, Object> baseInfo(Long userId) {
        Map<String, Object> result = new HashMap<>(4);

        Optional<Map<String, String>> userInfo = Optional.ofNullable(userMapper.getUserNameAndDepartmentName(userId));
        userInfo.ifPresent(item -> {
            result.put("flowCode", workFlowUtils.nextCode("CJXG"));
            result.put("userName", item.get("userName"));
            String flowName = item.get("partName") + item.get("userName") + "修改流程" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            result.put("flowName", flowName);
            result.put("partName", item.get("partName"));
            result.put("scenesList", Optional.ofNullable(dataResourceMapper.getScenes(userId)).orElse(null));
        });

        return result.size() == 0 ? null : result;
    }

    /**
     * 场景修改流程 数据
     * 场景图片及图片元素， 审批人信息
     * 图片点位继承父资源数据，父资源为基础资源返回空
     *
     * @param userId       userId
     * @param scenesDataId scenesDataId
     * @return map
     */
    public Map<String, Object> getScenesFlowData(Long userId, String scenesDataId) {
        Map<String, Object> result = new HashMap<>(2);
        TbDataResource dataResource = dataResourceMapper.getDataResourceById(scenesDataId);
        if (dataResource == null) {
            return null;
        }
        result.put("dataResource", dataResource);
        //组织id == null 是基础模型， 没有资源信息。
        if (dataResource.getDatabelong() == null) {
            Optional.ofNullable(modelPictureMapper.getModelPictureByScenesDataId(scenesDataId)).ifPresent(item -> {
                String dataId;
                for (Map<String, Object> map : item) {
                    //基础模型 id
                    dataId = map.get("baseModelId").toString();
                    //模型下图片 id
                    int pictureId = Integer.parseInt(map.get("pictureId").toString());
                    //基础模型id + 模型下图片id -> 获取图片下所有点位
                    List<Map<String, Object>> pictureAppendix = modelPictureAppendixMapper.getPictureAppendixByDataIdAndPictureId(dataId, pictureId);
                    if (pictureAppendix != null) {
                        map.put("appendixList", pictureAppendix);
                    }
                    map.remove("fatherModelId");

                }
                result.put("scenesPictureList", item);
                result.put("type", "create");
            });
        } else {
            TbWorkflowInfo workflowInfo = workflowInfoMapper.getSaveScenesWorkFlowInfoByDataId(scenesDataId);

            Long modelChangeId;
            if (workflowInfo == null) {
                modelChangeId = workflowModelChangeMapper.getLastCompletedModelChangeIdByDataId(scenesDataId);
            } else {
                result.put("infoId", workflowInfo.getId());
                modelChangeId = workflowModelChangeMapper.getByWorkflowInfoId(workflowInfo.getId()).getId();
            }

            Map<String, Object> scenesFlowData = getScenesFlowDataByInfoId(modelChangeId);
            result.put("scenesPictureList", scenesFlowData.get("list"));
            result.put("type", "modify");
        }

        TbWorkflowConfig workflowConfig = workflowConfigMapper.getWorkFlowConfigByWorkflowNoAndWorkflowOrder(1001, 2);
        Map<String, Object> approverMap = approverService.getApprover(userId, workflowConfig);
        result.put("approver", approverMap.get("approver"));
        return result;
    }

    /**
     * 获取场景数据
     *
     * @param modelChangeId modelChangeId
     * @return map
     */
    public Map<String, Object> getScenesFlowDataByInfoId(Long modelChangeId) {

        Map<String, Object> result = new HashMap<>(2);
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Map<String, Object>> appendixList = workflowModelChangeMapper.getAppendixByInfoId(modelChangeId);
        if (CollectionUtils.isEmpty(appendixList)) {
            return result;
        }

        List<Map<String, Object>> getPicture = workflowModelChangeMapper.getPicture(modelChangeId);
        Map<Integer, Object> pictureMap = Maps.newHashMap();
        for (Map<String, Object> map : getPicture) {
            pictureMap.put((Integer) map.get("pictureId"), map.get("picturePath"));
        }


        appendixList.stream().collect(Collectors.groupingBy(map -> (Integer) map.get("pictureId")))
                .forEach((key, value) -> {
                    Map<String, Object> pictureIdAppendix = new HashMap<>(3);
                    pictureIdAppendix.put("pictureId", key);
                    pictureIdAppendix.put("appendixList", value);
                    pictureIdAppendix.put("picturePath", pictureMap.get(key));
                    resultList.add(pictureIdAppendix);
                });
        result.put("list", resultList);


        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createWorkflow(CreateWorkflowParam createWorkflowParam) {

        //在已保存场景上再次操作删除原始内容重新创建
        if (createWorkflowParam.getInfoId() != null) {
            scenesFlowService.deleteWorkflow(createWorkflowParam.getInfoId());
        }

        Long infoId = scenesFlowService.createWorkflowInfo(createWorkflowParam);
        //提交
        if (createWorkflowParam.getOperationType().equals(1)) {
            scenesFlowService.createWorkflowLog(createWorkflowParam, infoId);
        }
        Long modelChangeId = scenesFlowService.createModelChange(createWorkflowParam, infoId);
        scenesFlowService.createChangeModel(createWorkflowParam, modelChangeId);
    }

    /**
     * insert tb_workflow_info
     *
     * @param createWorkflowParam createWorkflowParam
     * @return long
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createWorkflowInfo(CreateWorkflowParam createWorkflowParam) {
        Long userId = tokenService.getUserId(createWorkflowParam.getToken());

        TbWorkflowInfo workflowInfo = new TbWorkflowInfo();
        workflowInfo.setId(snowflakeIdGenerator.nextId());
        workflowInfo.setOrderno(createWorkflowParam.getWorkflowCode());
        workflowInfo.setWorkflowname(createWorkflowParam.getFlowName());
        workflowInfo.setOriginator(userId);
        workflowInfo.setCreatetime(new Date());
        workflowInfo.setWorkflowno(1001);
        workflowInfo.setCurrentworkfloworder(2);
        workflowInfo.setStatus(createWorkflowParam.getOperationType());
        workflowInfo.setWorkflowtype(1);
        workflowInfoMapper.insert(workflowInfo);
        return workflowInfo.getId();
    }

    /**
     * insert tb_workflow_log
     *
     * @param createWorkflowParam createWorkflowParam
     * @param infoId              infoId
     */
    @Transactional(rollbackFor = Exception.class)
    public void createWorkflowLog(CreateWorkflowParam createWorkflowParam, Long infoId) {
        Long userId = tokenService.getUserId(createWorkflowParam.getToken());
        TbUsers users = userMapper.selectById(userId);
        TbUsers approverUser = userMapper.selectById(createWorkflowParam.getApproverId());
        for (int i = 1; i <= 2; i++) {

            TbWorkflowLog workflowLog = new TbWorkflowLog();
            workflowLog.setId(snowflakeIdGenerator.nextId());
            if (i == 1) {
                workflowLog.setStatus(1);
                workflowLog.setUserid(userId);
                workflowLog.setDepartid(users.getDepartid());
            } else {
                workflowLog.setStatus(0);
                workflowLog.setUserid(createWorkflowParam.getApproverId());
                workflowLog.setDepartid(approverUser.getDepartid());
            }
            workflowLog.setMake("");
            workflowLog.setOptime(new Date());
            workflowLog.setWorkflowno(1001);
            workflowLog.setWorkfloworder(i);
            workflowLog.setWorkflowinfoid(infoId);
            workflowLogMapper.insert(workflowLog);
        }
    }

    /**
     * insert tb_workflow_model_change
     *
     * @param createWorkflowParam createWorkflowParam
     * @param infoId              infoId
     * @return long
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createModelChange(CreateWorkflowParam createWorkflowParam, Long infoId) {

        TbWorkflowModelChange workflowModelChange = new TbWorkflowModelChange();
        workflowModelChange.setId(snowflakeIdGenerator.nextId());
        workflowModelChange.setDataid(createWorkflowParam.getDataResource().getDataId());
        workflowModelChange.setWorkflowinfoid(infoId);
        workflowModelChange.setDatapath(createWorkflowParam.getDataResource().getDataPath());
        switch (createWorkflowParam.getType()) {
            case "create":
                workflowModelChange.setType(1);
                break;
            case "modify":
                workflowModelChange.setType(2);
                break;
            default:
                break;
        }
        workflowModelChange.setDataname(createWorkflowParam.getDataResource().getDataName());
        workflowModelChange.setDatatype(createWorkflowParam.getDataResource().getDataType());
        workflowModelChange.setAppendixpath(createWorkflowParam.getDataResource().getAppendixUrl());

        workflowModelChangeMapper.insert(workflowModelChange);

        return workflowModelChange.getId();
    }

    /**
     * insert tb_change_model
     *
     * @param createWorkflowParam createWorkflowParam
     * @param modelChangeId       modelChangeId
     */
    @Transactional(rollbackFor = Exception.class)
    public void createChangeModel(CreateWorkflowParam createWorkflowParam, Long modelChangeId) {
        for (CreateWorkflowParam.ScenesPicture picture : createWorkflowParam.getScenesPictureList()) {
            for (CreateWorkflowParam.Appendix appendix : picture.getAppendixList()) {

                TbChangeModel changeModel = new TbChangeModel();
                changeModel.setId(snowflakeIdGenerator.nextId());
                changeModel.setPictureid(picture.getPictureId());
                changeModel.setMateid(appendix.getMateId());
                changeModel.setMatetype(appendix.getMateType());
                changeModel.setMatepath(appendix.getMatePath());
                changeModel.setWorkflowmodelchangeid(modelChangeId);
                changeModelMapper.insert(changeModel);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteWorkflow(Long infoId) {

        Optional.ofNullable(workflowInfoMapper.selectById(infoId)).ifPresent(item -> {

            if (item.getStatus() == 0) {
                TbWorkflowModelChange workflowModelChange = workflowModelChangeMapper.getByWorkflowInfoId(infoId);

                QueryWrapper<TbChangeModel> changeModelQuery = new QueryWrapper<>();
                changeModelQuery.eq("workflowModelChangeId", workflowModelChange.getId());
                changeModelMapper.delete(changeModelQuery);

                workflowModelChangeMapper.deleteById(workflowModelChange.getId());

                workflowInfoMapper.deleteById(infoId);
            }
        });
    }
}
