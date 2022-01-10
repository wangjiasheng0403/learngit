package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

@Data
@ApiModel(value = "DepartmentParam")
public class DepartmentParam {

    @ApiModelProperty(value = "部门创建开始时间", required = false)
    private String startTime;

    @ApiModelProperty(value = "部门创建结束时间", required = false)
    private String endTime;

    @ApiModelProperty(value = "部门名称", required = false)
    private String departName;

    @ApiModelProperty(value = "分页页面下标，分页从1开始")
    private Integer pageIndex;

    @ApiModelProperty(value = "每页的记录数")
    private Integer pageSize;
}
