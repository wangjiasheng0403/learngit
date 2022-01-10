package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel(value = "HealthReviewParam")
public class HealthReviewParam {
    @NotBlank
    @ApiModelProperty(value = "要检查的内容")
    private String reviewDetails;
}
