package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ExportDepartmentParam")
public class ExportDepartmentParam {

    @ApiModelProperty(value = "部门创建开始时间", required = false)
    private String startTime;

    @ApiModelProperty(value = "部门创建结束时间", required = false)
    private String endTime;

    @ApiModelProperty(value = "部门名称", required = false)
    private String departName;
}
