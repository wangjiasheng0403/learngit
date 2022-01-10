package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author san
 */
@Data
@ApiModel(value = "CreateWorkflowParam")
public class CreateWorkflowParam {

    private Long infoId;

    @NotBlank
    @ApiModelProperty(value = "流程编号", required = true)
    private String workflowCode;

    @NotBlank
    @ApiModelProperty(value = "流程名", required = true)
    private String flowName;

    @ApiModelProperty(hidden = true)
    private String token;

    @NotNull
    @ApiModelProperty(value = "审批人", required = true)
    private Long approverId;

    @NotNull
    @ApiModelProperty(value = "0：保存，1：提交", required = true)
    private Integer operationType;

    @NotNull
    @ApiModelProperty(value = "create，modify", required = true)
    private String type;

    @NotNull
    @Size(min = 1)
    @ApiModelProperty(value = "scenesPictureList", required = true)
    private List<ScenesPicture> scenesPictureList;

    @NotNull
    @ApiModelProperty(value = "dataResource", required = true)
    private DataResource dataResource;

    @Data
    @ApiModel(value = "ScenesPicture")
    public static class ScenesPicture {
        @NotNull
        @ApiModelProperty(value = "pictureId", required = true)
        private Integer pictureId;
        @NotNull
        @Size(min = 1)
        @ApiModelProperty(value = "appendixList", required = true)
        private List<Appendix> appendixList;
    }

    @Data
    @ApiModel(value = "Appendix")
    public static class Appendix {
        @NotNull
        @ApiModelProperty(value = "pictureId", required = true)
        private String pictureId;
        @NotNull
        @ApiModelProperty(value = "mateId", required = true)
        private Integer mateId;
        @NotNull
        @ApiModelProperty(value = "mateType", required = true)
        private Integer mateType;
        @NotBlank
        @ApiModelProperty(value = "matePath", required = true)
        private String matePath;
    }

    @Data
    @ApiModel(value = "DataResource")
    public static class DataResource {
        @NotBlank
        @ApiModelProperty(value = "dataId", required = true)
        private String dataId;
        @NotBlank
        @ApiModelProperty(value = "dataName", required = true)
        private String dataName;
        @NotBlank
        @ApiModelProperty(value = "dataPath", required = true)
        private String dataPath;
        @NotNull
        @ApiModelProperty(value = "dataType", required = true)
        private Integer dataType;
        @NotBlank
        @ApiModelProperty(value = "appendixUrl", required = true)
        private String appendixUrl;
    }
}
