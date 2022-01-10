package org.zznode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zznode.common.CommonResult;
import org.zznode.dto.*;
import org.zznode.entity.TbUsersOptions;
import org.zznode.service.TbUsersOptionsService;
import org.zznode.service.impl.RoleLogsService;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(value = "/usersOptions")
@Api(tags = "用户操作记录")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersOptionsController {
    private final TbUsersOptionsService tbUsersOptionsService;


    @PostMapping(value = "/save")
    @ApiOperation(value = "保存终端用户操作记录")
    public CommonResult<?> save(@RequestBody UsersOptionParam param) {
        tbUsersOptionsService.save(param);
        return CommonResult.success();
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @PostMapping(value = "/list")
    @ApiOperation(value = "展示终端用户操作记录")
    public CommonResult<?> list(@RequestBody UsersOptionSearch param) {
        return CommonResult.success(tbUsersOptionsService.getPageList(param));
    }



}
