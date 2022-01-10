package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserCollectorParam")
public class UserCollectorParam {

    @ApiModelProperty(value = "用户ID")
    private Long userid;

    @ApiModelProperty(value = "资源名称")
    private String dataname;

    @ApiModelProperty(value = "资源路径")
    private String datapath;
}
