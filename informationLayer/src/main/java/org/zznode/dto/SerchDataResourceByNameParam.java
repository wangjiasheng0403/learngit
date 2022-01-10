package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "SerchDataResourceByNameParam")
public class SerchDataResourceByNameParam {

    @ApiModelProperty(value = "开始时间")
    private String starttime;

    @ApiModelProperty(value = "结束时间")
    private String endtime;

    @ApiModelProperty(value = "资源名称")
    private String dataname;

    @ApiModelProperty(value = "资源类型，公共场景管理请传入0，红色资源请传入1，党组织资源发布管理请传入2")
    private Integer datatype;

    @NotNull
    @ApiModelProperty(value = "分页页面下标，分页从1开始")
    private Integer pageIndex;

    @NotNull
    @ApiModelProperty(value = "每页的记录数")
    private Integer pageSize;

}
