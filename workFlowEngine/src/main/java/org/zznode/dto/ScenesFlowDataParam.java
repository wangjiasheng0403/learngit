package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel(value = "ScenesFlowDataParam")
public class ScenesFlowDataParam {

    @ApiModelProperty(hidden = true)
    private String token;

    @NotBlank
    @ApiModelProperty(value = "dataId", required = true)
    private String dataId;
}
