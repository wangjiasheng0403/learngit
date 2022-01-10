package org.zznode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.zznode.common.CommonResult;
import org.zznode.dao.TbUsersMapper;
import org.zznode.dto.TbWorkflowOperationAccountChangeParam;
import org.zznode.entity.TbUsers;
import org.zznode.entity.TbWorkflowInfo;
import org.zznode.entity.TbWorkflowLog;
import org.zznode.service.*;
import org.zznode.util.FileUtils;
import org.zznode.util.SnowflakeIdGenerator;

import java.util.Date;
import java.util.Map;

/**
 * 运营账号流程中心
 */
@RestController
@RequestMapping("/workflowaccount")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "内容运营方帐号管理")
public class TbWorkflowOperationAccountChangeController {

    private final TbWorkflowOperationAccountChangeService tbWorkflowOperationAccountChangeService;
    private final TbWorkflowConfigService tbWorkflowConfigService;
    private final TbWorkflowInfoService tbWorkflowInfoService;
    private final TbWorkflowLogService tbWorkflowLogService;
    private final TbUsersService tbUsersService;
    private final TokenService tokenService;
    private final TbUsersMapper tbUsersMapper;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final Integer workflowNo = 1002;

    /**
     * 删除运营账号流程申请
     */
    @PostMapping("/apply")
    @ApiOperation(value = "删除运营账号流程申请")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> apply(@RequestHeader("Authorization") String token, @RequestBody TbWorkflowOperationAccountChangeParam param) {
        param.setToken(token);
        TbUsers users = tokenService.getUser(param.getToken());
        //通过userId查询用户信息
        Map<String, String> userMap = tbUsersService.selectById(param.getDeleteUserId());
        //创建工作流tb_workflow_info
        TbWorkflowInfo tbWorkflowInfo = new TbWorkflowInfo();
        tbWorkflowInfo.setId(snowflakeIdGenerator.nextId());
        tbWorkflowInfo.setOrderno(param.getOrderNo());
        tbWorkflowInfo.setWorkflowname("运营账号删除申请：" + userMap.get("UserName"));
        tbWorkflowInfo.setOriginator(users.getUserid());
        tbWorkflowInfo.setCreatetime(new Date());
        tbWorkflowInfo.setWorkflowno(workflowNo);
        tbWorkflowInfo.setCurrentworkfloworder(2);
        tbWorkflowInfo.setStatus(param.getStatus());
        tbWorkflowInfo.setWorkflowtype(2);
        tbWorkflowInfoService.save(tbWorkflowInfo);
        //创建工作流审批步骤tb_workflow_log 先查询出workflow_config workflowNo = 102 申请人和下级审批人
        for (int i = 1; i <= 2; i++) {
            TbWorkflowLog tbWorkflowLog = new TbWorkflowLog();
            tbWorkflowLog.setId(snowflakeIdGenerator.nextId());
            tbWorkflowLog.setRoleid(param.getRoleId());
            if(i == 1){
                // log 1 申请人
                tbWorkflowLog.setStatus(1);
                tbWorkflowLog.setUserid(users.getUserid());
                tbWorkflowLog.setDepartid(users.getParyid());
                tbWorkflowLog.setMake(param.getDeleteContent());
            }else{
                // log 2 审核人
                tbWorkflowLog.setStatus(0);
                tbWorkflowLog.setUserid(param.getApproverUserID());
                TbUsers tbUsers = tbUsersMapper.selectById(param.getApproverUserID());
                tbWorkflowLog.setDepartid(tbUsers.getParyid());
                tbWorkflowLog.setMake("");
            }
            tbWorkflowLog.setWorkflowno(workflowNo);
            tbWorkflowLog.setWorkfloworder(i);
            tbWorkflowLog.setWorkflowinfoid(tbWorkflowInfo.getId());
            tbWorkflowLogService.save(tbWorkflowLog);
        }

        //创建运营账号数据审批内容数据tb_workflow_operation_account_change
        userMap.put("filePath", param.getUploadFilePath());
        userMap.put("deleteContent",param.getDeleteContent());
        tbWorkflowOperationAccountChangeService.save(tbWorkflowInfo.getId(), userMap);
        return CommonResult.success();
    }

    /**
     * 附件上传
     *
     * @return
     */
    @PostMapping(value = "uploadFile")
    @ApiOperation(value = "上传")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前操作时，上一次上传的文件名", name = "oldFileName", dataType = "String"),
            @ApiImplicitParam(value = "文件", name = "file", dataType = "MultipartFile", required = true)})
    public CommonResult<?> uploadFile(MultipartHttpServletRequest request) {
        return CommonResult.success(FileUtils.upload(FileUtils.WorkFlow.OPERATOR, FileUtils.FileType.ATTACHMENT, request.getFile("file")));
    }

}
