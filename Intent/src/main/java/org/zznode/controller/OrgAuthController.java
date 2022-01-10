package org.zznode.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.zznode.dto.GetOrgAuthTreeParam;
import org.zznode.dto.ModifyAuthParam;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("orgAuth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrgAuthController {

    private final RestTemplate restTemplate;

    @PostMapping("/getOrgAuthTree")
    @ApiOperation(value = "获取授权树")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public JSONObject getOrgAuthTree(@RequestHeader("Authorization") String token,
                                     @Valid @RequestBody GetOrgAuthTreeParam param) {

        param.setToken(token);
        return restTemplate.postForEntity("http://informationLayer/orgAuth/getOrgAuthTree", param, JSONObject.class).getBody();
    }

    @PostMapping("/modifyAuth")
    @ApiOperation(value = "修改授权组织")
    public JSONObject modifyAuth(@Valid @RequestBody ModifyAuthParam param) {
        return restTemplate.postForEntity("http://informationLayer/orgAuth/modifyAuth", param, JSONObject.class).getBody();

    }
}
