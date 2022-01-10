package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "ModifyOrganizationParam")
public class ModifyOrganizationParam {
    @ApiModelProperty(hidden = true)
    private String token;

    @NotBlank
    @ApiModelProperty(value = "附件", required = true)
    private String attachment;
    @NotBlank
    @ApiModelProperty(value = "党组织名", required = true)
    private String organizationName;
    @NotNull
    @ApiModelProperty(value = "上级党组织id", required = true)
    private Long parentOrganizationId;
    @ApiModelProperty(value = "workflowNo")
    private String workflowNo;
    @ApiModelProperty(value = "workflowInfoId")
    private Long workflowInfoId;
    /**
     * 审核人
     */
    @ApiModelProperty(value = "审核人")
    private Long approverId;
    /**
     * 状态
     * 0保存
     * 1进行中
     */
    @ApiModelProperty(value = "状态 0保存，1进行中")
    private Integer commitStatus;
    private Long partID;
}
