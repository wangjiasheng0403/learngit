package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.zznode.common.LogCompar;

import java.util.List;

@Data
@ApiModel(value = "TbUserSearch")
public class TbUserParam {
    @LogCompar("tokem")
    @ApiModelProperty(hidden = true)
    private String token;

    @LogCompar("用户ID")
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @LogCompar("党组织ID")
    @ApiModelProperty(value = "党组织ID")
    private Long paryId;

    @LogCompar("密码")
    @ApiModelProperty(value = "密码")
    private String password;

    @LogCompar("账号")
    @ApiModelProperty(value = "账号")
    private String account;

    @LogCompar("姓名")
    @ApiModelProperty(value = "姓名")
    private String username;

    @LogCompar("电话")
    @ApiModelProperty(value = "电话")
    private String phone;

    @LogCompar("邮箱")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @LogCompar("性别")
    @ApiModelProperty(value = "性别")
    private String sex;

    @LogCompar("职务")
    @ApiModelProperty(value = "职务")
    private String duty;

    @LogCompar("用户状态")
    @ApiModelProperty(value = "1：启用，0：禁用")
    private String accountStatus;

    @LogCompar("角色")
    @ApiModelProperty(value = "rolesList")
    private List<TbRolesParam> rolesList;
}
