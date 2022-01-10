package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "UpdateRoleInfoParam")
public class UpdateRoleInfoParam {

    @ApiModelProperty(value = "角色名称")
    private String rolesname;

    @ApiModelProperty(value = "需要修改的角色ID")
    private Long rolesid;

    @ApiModelProperty(value = "修改后的描述内容")
    private String descriptor;

    @ApiModelProperty(value = "修改后的权限")
    private List<Long>  authority;
}
