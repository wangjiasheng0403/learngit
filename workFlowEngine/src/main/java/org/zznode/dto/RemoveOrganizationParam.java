package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "RemoveOrganizationParam")
public class RemoveOrganizationParam {
    @ApiModelProperty(hidden = true)
    private String token;
    @NotNull
    @ApiModelProperty(value = "organizationId", required = true)
    private Long organizationId;
    /**
     * 状态
     * 0保存
     * 1进行中
     */
    @ApiModelProperty(value = "状态 0保存，1进行中")
    private Integer commitStatus;
    /**
     *
     */
    @NotBlank
    @ApiModelProperty(value = "附件", required = true)
    private String attachment;

    @ApiModelProperty(value = "workflowNo")
    private String workflowNo;

    /**
     * 审核人
     */
    @ApiModelProperty(value = "审核人")
    private Long approverId;
    @ApiModelProperty(value = "workflowInfoId")
    private Long workflowInfoId;
    private Long partID;
}
