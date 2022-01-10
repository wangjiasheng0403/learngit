package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ChangepwdParam")
public class ChangepwdParam {

    @ApiModelProperty(value = "用户ID")
    private Long usersid;

    @ApiModelProperty(value = "旧密码")
    private String oldpwd;

    @ApiModelProperty(value = "新密码")
    private String newpwd;
}
