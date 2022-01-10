package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "SerchRoleInfoAllParam")
public class SerchRoleInfoAllParam {
    @NotNull
    @ApiModelProperty(value = "分页页面下标，分页从1开始")
    private Integer pageIndex;

    @NotNull
    @ApiModelProperty(value = "每页的记录数")
    private Integer pageSize;
}
