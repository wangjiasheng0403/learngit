package org.zznode.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.zznode.dto.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/roleLogs")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleLogsController {

    private final RestTemplate restTemplate;

    @GetMapping(value = "/hello")
    public String hello() {
        return "hello. roleLogs.";
    }

    @PostMapping(value = "/save")
    public JSONObject save(@RequestBody RoleLogParam param) {

        return restTemplate.postForEntity("http://informationLayer/roleLogs/save", param, JSONObject.class).getBody();
    }

    @PostMapping(value = "/getRolesLogList")
    public JSONObject getRolesLogList(@RequestBody RoleLogsSearch param) {
        return restTemplate.postForEntity("http://informationLayer/roleLogs/getRolesLogList", param, JSONObject.class).getBody();
    }

    @PostMapping(value = "/getLoginViewRecords")
    public JSONObject getLoginViewRecords(@RequestBody RoleLogsSearch param) {
        return restTemplate.postForEntity("http://informationLayer/roleLogs/getLoginViewRecords", param, JSONObject.class).getBody();
    }

    @PostMapping(value = "/getOperateRecords")
    public JSONObject getOperateRecords(@RequestBody RoleLogsSearch param) {
        return restTemplate.postForEntity("http://informationLayer/roleLogs/getOperateRecords", param, JSONObject.class).getBody();
    }

    @PostMapping("/getDataResource")
    public JSONObject getDataResource() {

        return restTemplate.postForEntity("http://informationLayer/roleLogs/getDataResource", "", JSONObject.class).getBody();
    }

    @PostMapping(value = "/getRoleLogs")
    @ApiOperation(value = "获取浏览统计记录")
    public JSONObject getRoleLogs(@Valid @RequestBody GetRoleLogsParam param) {
        return restTemplate.postForEntity("http://informationLayer/roleLogs/getRoleLogs", param, JSONObject.class).getBody();
    }

    @PostMapping(value = "/visitingDetail")
    @ApiOperation(value = "访问明细")
    public JSONObject visitingDetail(@Valid @RequestBody VisitingDetailParam param) {
        return restTemplate.postForEntity("http://informationLayer/roleLogs/visitingDetail", param, JSONObject.class).getBody();
    }

    @PostMapping(value = "/viewComments")
    @ApiOperation(value = "查看评论")
    public JSONObject viewComments(@Valid @RequestBody ViewCommentsParam param) {
        return restTemplate.postForEntity("http://informationLayer/roleLogs/viewComments", param, JSONObject.class).getBody();
    }

    @PostMapping(value = "/commentsDetail")
    @ApiOperation(value = "查看评论")
    public JSONObject commentsDetail(@Valid @RequestBody CommentsDetailParam param) {
        return restTemplate.postForEntity("http://informationLayer/roleLogs/commentsDetail", param, JSONObject.class).getBody();
    }

    @PostMapping(value = "/getUsersOptionsList")
    @ApiOperation(value = "终端用户修改记录")
    public JSONObject getUsersOptionsList(@Valid @RequestBody UsersOptionSearch param) {
        return restTemplate.postForEntity("http://informationLayer/usersOptions/list", param, JSONObject.class).getBody();
    }

    @PostMapping(value = "/usersOptionsSave")
    @ApiOperation(value = "终端用户修改保存")
    public JSONObject usersOptionsSave(@Valid @RequestBody UsersOptionParam param) {
        return restTemplate.postForEntity("http://informationLayer/usersOptions/save", param, JSONObject.class).getBody();
    }
}
