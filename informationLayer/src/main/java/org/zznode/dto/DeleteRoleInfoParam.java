package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DeleteRoleInfoParam")
public class DeleteRoleInfoParam {

    @ApiModelProperty(value = "需要删除的角色ID")
    private Long rolesid;
}
