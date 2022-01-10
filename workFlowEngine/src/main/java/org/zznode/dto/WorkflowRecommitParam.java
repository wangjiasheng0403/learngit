package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel(value = "WorkflowRecommitParam")
public class WorkflowRecommitParam {

    @ApiModelProperty(hidden = true)
    private String token;

    @NotNull
    @ApiModelProperty(value = "workflowInfoId", required = true)
    private Long workflowInfoId;
    /**
     * 下一个审核人的id
     */
    @NotNull
    @ApiModelProperty(value = "下一个审核人的id", required = true)
    private Long approverId;

    /**
     * 场景名称
     */
    @ApiModelProperty(value = "场景名称")
    private String dataName;
    /**
     * 审核意见
     */
    @ApiModelProperty(value = "审核意见")
    private String make;
    @NotNull
    @ApiModelProperty(value = "workflowLogId", required = true)
    private Long workflowLogId;
    /**
     * 附件
     */
    @NotBlank
    @ApiModelProperty(value = "附件", required = true)
    private String attachment;

    @NotNull
    @Size(min = 1)
    @ApiModelProperty(value = "附件", required = true)
    private List<ModelChange> modelChangeList;

    @Data
    @ApiModel(value = "ModelChange")
    public static class ModelChange {
        @ApiModelProperty(value = "id")
        private Long id;
        @NotBlank
        @ApiModelProperty(value = "保存url", required = true)
        private String matePath;
    }
}
