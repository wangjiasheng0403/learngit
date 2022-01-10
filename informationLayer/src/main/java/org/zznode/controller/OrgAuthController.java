package org.zznode.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zznode.common.CommonResult;
import org.zznode.dto.GetOrgAuthTreeParam;
import org.zznode.dto.ModifyAuthParam;
import org.zznode.service.impl.OrgAuthTreeService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("orgAuth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrgAuthController {

    private final OrgAuthTreeService orgAuthTreeService;

    @PostMapping("/getOrgAuthTree")
    @ApiOperation(value = "获取授权树")
    public CommonResult<?> getOrgAuthTree(@RequestBody GetOrgAuthTreeParam param) {
        return CommonResult.success(orgAuthTreeService.getOrgAuthTree(param));
    }

    @PostMapping("/modifyAuth")
    @ApiOperation(value = "修改授权组织")
    public CommonResult<?> modifyAuth(@Valid @RequestBody ModifyAuthParam param) {
        orgAuthTreeService.modifyAuth(param);
        return CommonResult.success();
    }
}
