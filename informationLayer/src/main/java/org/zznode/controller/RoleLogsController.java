package org.zznode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zznode.common.CommonResult;
import org.zznode.dto.*;
import org.zznode.service.impl.RoleLogsService;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(value = "/roleLogs")
@Api(tags = "用户行为日志")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleLogsController {
    private final RoleLogsService roleLogsService;

    @GetMapping(value = "/hello")
    public String hello() {
        return "hello. roleLogs.";
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @PostMapping(value = "/save")
    @ApiOperation(value = "保存")
    public CommonResult<?> save(@RequestBody RoleLogParam param) {
        System.out.println("param" + param);
        roleLogsService.save(param);
        return CommonResult.success();
    }

    @PostMapping("/getDataResource")
    @ApiOperation(value = "场景list")
    public CommonResult<?> getDataResource(@Valid @RequestBody DataResourceParam param) {
        return CommonResult.success(roleLogsService.getDataResource(param));
    }

    @PostMapping(value = "/getRoleLogs")
    @ApiOperation(value = "获取浏览统计记录")
    public CommonResult<?> getRoleLogs(@Valid @RequestBody GetRoleLogsParam param) {
        System.out.println("param:" + param);
        return CommonResult.success(roleLogsService.getRoleLogs(param));
    }

    @PostMapping(value = "/visitingDetail")
    @ApiOperation(value = "访问明细")
    public CommonResult<?> visitingDetail(@Valid @RequestBody VisitingDetailParam param) {
        return CommonResult.success(roleLogsService.visitingDetail(param));
    }

    @PostMapping(value = "/getRolesLogList")
    @ApiOperation(value = "终端用户记录列表")
    public CommonResult<?> getRoleLogsList(@RequestBody RoleLogsSeachList param) {
        return CommonResult.success(roleLogsService.getRoleLogsList(param));
    }

    @PostMapping(value = "/getLoginViewRecords")
    @ApiOperation(value = "登录用户场景记录")
    public CommonResult<?> getLoginViewRecords(@RequestBody RoleLogsSearch param) {
        return CommonResult.success(roleLogsService.getLoginViewRecords(param));
    }

    @PostMapping(value = "/getOperateRecords")
    @ApiOperation(value = "场景内操作记录")
    public CommonResult<?> getOperateRecords(@RequestBody RoleLogsSearch param) {
        return CommonResult.success(roleLogsService.getOperateRecords(param));
    }

    @PostMapping(value = "/viewComments")
    @ApiOperation(value = "查看评论")
    public CommonResult<?> viewComments(@Valid @RequestBody ViewCommentsParam param) {
        return CommonResult.success(roleLogsService.viewComments(param));
    }

    @PostMapping(value = "/commentsDetail")
    @ApiOperation(value = "查看评论")
    public CommonResult<?> commentsDetail(@Valid @RequestBody CommentsDetailParam param) {
        return CommonResult.success(roleLogsService.commentsDetail(param));
    }
}
