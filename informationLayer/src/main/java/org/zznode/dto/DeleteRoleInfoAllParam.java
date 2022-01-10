package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "DeleteRoleInfoAllParam")
public class DeleteRoleInfoAllParam {

    @ApiModelProperty(value = "需要删除的角色ID的long数组")
    private String rolesid;
}
