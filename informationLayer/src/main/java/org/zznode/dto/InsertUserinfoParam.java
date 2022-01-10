package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "InsertUserinfoParam")
public class InsertUserinfoParam {

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "用户手机号码")
    private String phone;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门ID")
    private Long departid;

    @ApiModelProperty(value = "所属组织ID")
    private Long paryid;

    @ApiModelProperty(value = "角色ID")
    private String rolesid;

    @ApiModelProperty(value = "是否启用 1为启用")
    private String acountstatus;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "行政职务字典表ID")
    private Long departdutyid;

    @ApiModelProperty(value = "党组织职务字典表ID")
    private Long parydutyid;
}
