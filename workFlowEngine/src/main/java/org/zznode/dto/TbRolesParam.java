package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TbUserSearch")
public class TbRolesParam {

    @ApiModelProperty(value = "rolesId")
    private Long rolesId;
}
