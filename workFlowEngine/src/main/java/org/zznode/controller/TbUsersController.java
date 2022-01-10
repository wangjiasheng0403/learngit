package org.zznode.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.zznode.common.CommonResult;
import org.zznode.common.CustomException;
import org.zznode.dto.TbUserDeleteParam;
import org.zznode.dto.TbUserParam;
import org.zznode.dto.TbUserSearch;
import org.zznode.entity.TbUsers;
import org.zznode.entity.TbWorkflowConfig;
import org.zznode.service.*;
import org.zznode.util.WorkFlowUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "内容运营方帐号管理")
public class TbUsersController {
    private final TbUsersService tbUsersService;
    private final ApproverService approverService;
    private final TbWorkflowConfigService tbWorkflowConfigService;
    private final WorkFlowUtils workFlowUtils;
    private final TokenService tokenService;
    private final TbWorkflowOperationAccountChangeService tbWorkflowOperationAccountChangeService;


    /**
     * 党组织树形数据
     *
     * @return
     */
    @PostMapping(value = "paryTree")
    @ApiOperation(value = "党组织树形数据")
    public CommonResult<?> paryTree() {
        return CommonResult.success(tbUsersService.paryTree());
    }

    /**
     * 运营账号分页列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "获取场景数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> list(@RequestHeader("Authorization") String token, @RequestBody TbUserSearch param) {
        param.setToken(token);
        return CommonResult.success(tbUsersService.getPageList(param));
    }

    /**
     * 查看详情明细
     */
    @PostMapping("/getUserInfo")
    @ApiOperation(value = "查看详情明细")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> getUserInfo(@RequestHeader("Authorization") String token, @RequestBody TbUserDeleteParam param) {
        param.setToken(token);
        Map<String, Object> resutltMap = new HashMap<>();
        Map<String, String> userMap = tbUsersService.selectById(param.getUserId());
        resutltMap.putAll(userMap);
        resutltMap.put("roleList", tbUsersService.getRoles());
        resutltMap.put("paryTree", tbUsersService.paryTree());
        return CommonResult.success(resutltMap);
    }

    /**
     * 更新账号状态
     */
    @PostMapping("/updateStatus")
    @ApiOperation(value = "更新账号状态")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> updateStatus(@RequestHeader("Authorization") String token, @RequestBody TbUserParam param) {
        param.setToken(token);
        tbUsersService.updateStatus(param.getUserId(), param.getAccountStatus());
        return CommonResult.success();
    }

    /**
     * 查询所有角色
     */
    @PostMapping("/getRoles")
    @ApiOperation(value = "查询所有角色")
    public CommonResult<?> getRoles() {
        return CommonResult.success(tbUsersService.getRoles());
    }

    /**
     * 通过主键查询详细数据
     */
    @PostMapping("/getById")
    @ApiOperation(value = "通过主键查询详细数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> getById(@RequestHeader("Authorization") String token, @RequestBody TbUserDeleteParam param) {
        //当前被删除人是否已存在流程
        Map<String, Object> resultMap = new HashMap<>();
        TbUsers tbUsers = new TbUsers();
        param.setToken(token);
        tbUsers = tokenService.getUser(param.getToken());
        Map<String, Object> saveMap = tbWorkflowOperationAccountChangeService.getSaveData(param.getDeleteUserId());
        if (ObjectUtils.isEmpty(saveMap)) {
            resultMap.put("orderNo", workFlowUtils.nextCode("YYFSC"));//流程单号
        } else {
            resultMap.put("orderNo", saveMap.get("orderNo"));//流程单号
            tbUsers.setUserid(Long.parseLong(saveMap.get("userId").toString()));
            resultMap.put("filePath", saveMap.get("filePath"));//附件
            resultMap.put("deleteContent", saveMap.get("deleteContent"));//删除理由
        }

        //查询当前申请人信息
        Map<String, String> loginMap = tbUsersService.selectById(tbUsers.getUserid());
        resultMap.put("loginuserId", tbUsers.getUserid());//申请人ID
        resultMap.put("loginName", loginMap.get("UserName"));//申请人name
        resultMap.put("loginDepartName", loginMap.get("departName"));//申请人所属行政机构
        resultMap.put("loginParyName", loginMap.get("partName"));//申请人所属党机构
        //查询运营账号删除人员信息
        Map<String, String> userMap = tbUsersService.selectById(param.getDeleteUserId());
        resultMap.put("workflowName", "内容运营方人员删除流程");//流程名称
        resultMap.put("deleteUserId", param.getDeleteUserId());//被删除人ID
        //resultMap.put("userName", tbUsers.getUsername());//申请人Name
        resultMap.put("deleteaccount", userMap.get("Account"));//被删除人账号
        resultMap.put("deleteparyId", userMap.get("partID"));//被删除人所属党组织ID
        resultMap.put("deleteparyName", userMap.get("partName"));//被删除人所属党组织Name
        resultMap.put("deletedepartId", userMap.get("departID"));//被删除人所属行政组织ID
        resultMap.put("deletedepartName", userMap.get("departName"));//被删除人所属行政组织Name
        resultMap.put("deleteaccountRolesName", userMap.get("rolesName"));//被删除人账号角色name
        resultMap.put("deleteaccountRolesIds", userMap.get("rolesId"));//被删除人账号角色ID
        //通过当前登录人查询审批流程信息
        TbWorkflowConfig tbWorkflowConfig = tbWorkflowConfigService.getWorkFlowConfigByWorkflowNoAndWorkflowOrder(1002, 2);
        Map<String, Object> paryMap = approverService.getApprover(tbUsers.getUserid(), tbWorkflowConfig);
        resultMap.putAll(paryMap);
        return CommonResult.success(resultMap);
    }

    /**
     * 保存运营账号数据
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存运营账号数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> save(@RequestHeader("Authorization") String token, @RequestBody TbUserParam param) {
        param.setToken(token);
        //tbUsersService.save(param);
        return tbUsersService.selectUnique(param);
    }

    /**
     * 修改运营账号数据
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改运营账号数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> update(@RequestHeader("Authorization") String token, @RequestBody TbUserParam param) {
        param.setToken(token);
        if (0 == param.getUserId() || null == param.getUserId()) {
            return CommonResult.error(new CustomException("501", "参数错误", "更新数据时，主键不能为空！"));
        }
        //tbUsersService.update(param);
        return tbUsersService.selectUnique(param);
    }
}
