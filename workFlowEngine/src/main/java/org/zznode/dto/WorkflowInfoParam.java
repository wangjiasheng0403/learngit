package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "WorkflowInfoParam")
public class WorkflowInfoParam {
    @ApiModelProperty(hidden = true)
    private String token;
    @NotNull
    @ApiModelProperty(value = "workflowInfoId", required = true)
    private Long workflowInfoId;
}
