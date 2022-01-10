package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SetPasswdByVerificationCodeParam")
public class SetPasswdByVerificationCodeParam {
    @ApiModelProperty(value = "用户账号")
    private String userAccount;

    @ApiModelProperty(value = "验证码")
    private String verificationCode;

    @ApiModelProperty(value = "新密码")
    private String passwd;
}
