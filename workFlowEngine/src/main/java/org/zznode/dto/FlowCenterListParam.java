package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "FlowCenterListParam")
public class FlowCenterListParam {
    @ApiModelProperty(hidden = true)
    private String token;
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
    /**
     * 流程状态
     */
    @NotNull
    @ApiModelProperty(value = "流程状态 1: 代办，2：已办，3：发起，4：完成", example = "1", required = true)
    private Integer workflowStatus;
}
