package org.zznode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zznode.common.CommonResult;
import org.zznode.dto.CreateWorkflowParam;
import org.zznode.dto.ScenesFlowDataParam;
import org.zznode.service.ScenesFlowService;
import org.zznode.service.TokenService;
import org.zznode.util.FileUtils;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/scenesFlow")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "场景修改流程")
public class ScenesController {

    private final ScenesFlowService service;
    private final TokenService tokenService;

    @GetMapping("/hello")
    public String hello() {
        return "hello. scenesFlow.1-5";
    }

    @PostMapping(value = "getBaseInfo")
    @ApiOperation(value = "场景修改基本信息")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> baseInfo(@RequestHeader("Authorization") String token) {
        Long userId = tokenService.getUserId(token);
        return CommonResult.success(service.baseInfo(userId));
    }

    @PostMapping("/getScenesFlowData")
    @ApiOperation(value = "获取场景数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> getScenesFlowData(@RequestHeader("Authorization") String token,
                                             @Valid @RequestBody ScenesFlowDataParam param) {
        param.setToken(token);
        Long userId = tokenService.getUserId(param.getToken());
        return CommonResult.success(service.getScenesFlowData(userId, param.getDataId()));
    }

    @PostMapping("/submitScenes")
    @ApiOperation(value = "保存/提交")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> submitScenes(@RequestHeader("Authorization") String token,
                                        @Valid @RequestBody CreateWorkflowParam param) {
        param.setToken(token);
        service.createWorkflow(param);
        return CommonResult.success();
    }

    @PostMapping("/upload")
    @ApiOperation(value = "上传")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前操作时，上一次上传的文件名", name = "oldFileName", dataType = "String", required = true),
            @ApiImplicitParam(value = "文件类型 audio，video，picture，attachment ", name = "fileType", dataType = "String", required = true),
            @ApiImplicitParam(value = "文件", name = "file", dataType = "MultipartFile", required = true)})
    public CommonResult<?> upload(@RequestParam(value = "oldFileName", required = false) String oldFileName,
                                  @RequestParam(value = "fileType") String type,
                                  @RequestParam(value = "file") MultipartFile file) {
        FileUtils.FileType fileType;
        switch (type) {
            case "audio":
                fileType = FileUtils.FileType.AUDIO;
                break;
            case "video":
                fileType = FileUtils.FileType.VIDEO;
                break;
            case "picture":
                fileType = FileUtils.FileType.PICTURE;
                break;
            default:
                fileType = FileUtils.FileType.ATTACHMENT;
                break;
        }

        return CommonResult.success(FileUtils.upload(FileUtils.WorkFlow.SCENES, fileType, file, oldFileName));
    }
}
