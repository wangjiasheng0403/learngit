package org.zznode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zznode.common.CommonResult;
import org.zznode.common.CustomException;
import org.zznode.dto.FlowCenterListParam;
import org.zznode.dto.WorkflowApprovalParam;
import org.zznode.dto.WorkflowInfoParam;
import org.zznode.dto.WorkflowRecommitParam;
import org.zznode.service.FlowCenterService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * 流程中心控制器
 */
@RestController
@RequestMapping("/flowCenter")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "流程中心")
public class FlowCenterController {

    private final FlowCenterService flowCenterService;

    /**
     * 流程中心列表-待办列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "流程中心列表-待办列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> list(@RequestHeader("Authorization") String token,
                                @Valid @RequestBody FlowCenterListParam param) {
        param.setToken(token);
        if (param.getWorkflowStatus() == 1) {
            return CommonResult.success(flowCenterService.getPageListForBacklog(param));
        } else if (param.getWorkflowStatus() == 2) {
            return CommonResult.success(flowCenterService.getPageListForDone(param));
        } else if (param.getWorkflowStatus() == 3) {
            return CommonResult.success(flowCenterService.getPageListForOriginate(param));
        } else if (param.getWorkflowStatus() == 4) {
            return CommonResult.success(flowCenterService.getPageListForFinished(param));
        } else {
            throw new CustomException("000001", "流程状态错误", "流程状态错误【1/2/3/4】");
        }
    }

    /**
     * 获取模型流程信息
     */
    @PostMapping("/modelInfo")
    @ApiOperation(value = "获取模型流程信息")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> modelInfo(@RequestHeader("Authorization") String token,
                                     @Valid @RequestBody WorkflowInfoParam param) {
        param.setToken(token);
        return CommonResult.success(flowCenterService.getModelInfo(param));
    }

    /**
     * 获取运营账号流程信息
     */
    @PostMapping("/operationAccountInfo")
    @ApiOperation(value = "获取运营账号流程信息")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> getOperationAccountInfo(@RequestHeader("Authorization") String token,
                                                   @Valid @RequestBody WorkflowInfoParam param) {
        param.setToken(token);
        return CommonResult.success(flowCenterService.getOperationAccountInfo(param));
    }

    /**
     * 获取组织流程信息
     */
    @PostMapping("/organizationInfo")
    @ApiOperation(value = "获取组织流程信息")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> getOrganizationInfo(@RequestHeader("Authorization") String token,
                                               @Valid @RequestBody WorkflowInfoParam param) {
        param.setToken(token);
        return CommonResult.success(flowCenterService.getOrganizationInfo(param));
    }

    /**
     * 重新提交审批
     */
    @PostMapping("/modelRecommit")
    @ApiOperation(value = "重新提交审批")
    @ApiIgnore
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> modelRecommit(@RequestHeader("Authorization") String token,
                                         @Valid @RequestBody WorkflowRecommitParam param) {
        param.setToken(token);
        flowCenterService.modelRecommit(param);
        return CommonResult.success();
    }

    /**
     * 审批
     */
    @PostMapping("/approval")
    @ApiOperation(value = "审批")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> approval(@RequestHeader("Authorization") String token,
                                    @Valid @RequestBody WorkflowApprovalParam param) {
        param.setToken(token);
        flowCenterService.approval(param);
        return CommonResult.success();
    }

}
