package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ListOrganizationParam")
public class InitOrganizationParam {
    @ApiModelProperty(hidden = true)
    private String token;
    @ApiModelProperty(value = "organizationId")
    private Long organizationId;
}
