package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "GetRoleLogsParam")
public class GetRoleLogsParam {

    @ApiModelProperty(value = "场景Id")
    private String dataId;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    /**
     * 当前页
     */
    @NotNull
    @ApiModelProperty(value = "当前页", example = "1", required = true)
    private Integer pageNo;
    
    /**
     * 分页大小
     */
    @NotNull
    @ApiModelProperty(value = "分页大小", example = "10", required = true)
    private Integer pageSize;
}
