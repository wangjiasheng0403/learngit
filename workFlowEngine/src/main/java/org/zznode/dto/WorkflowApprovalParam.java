package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "WorkflowApprovalParam")
public class WorkflowApprovalParam {
    @ApiModelProperty(hidden = true)
    private String token;
    @NotNull
    @ApiModelProperty(value = "workflowInfoId", required = true)
    private Long workflowInfoId;
    /**
     * 处理结果（1通过 2拒绝）
     */
    @NotNull
    @ApiModelProperty(value = "处理结果（1通过 2拒绝）", example = "1", required = true)
    private Integer opStatus;
    /**
     * 处理评语
     */
    @NotBlank
    @ApiModelProperty(value = "处理评语", required = true)
    private String make;
    /**
     *
     */
    @NotNull
    @ApiModelProperty(value = "workflowLogId", required = true)
    private Long workflowLogId;
    /**
     * 下一个审核人的id
     */
    @ApiModelProperty(value = "下一个审核人的id")
    private Long approverId;
}
