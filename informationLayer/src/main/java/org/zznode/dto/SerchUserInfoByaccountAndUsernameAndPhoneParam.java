package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SerchUserInfoByaccountAndUsernameAndPhoneParam")
public class SerchUserInfoByaccountAndUsernameAndPhoneParam {

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "用户手机号码")
    private String phone;

    @ApiModelProperty(value = "分页页面下标，分页从1开始")
    private Integer pageIndex;

    @ApiModelProperty(value = "每页的记录数")
    private Integer pageSize;

}
