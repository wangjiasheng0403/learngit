package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "InsertRoleInfoparam")
public class InsertRoleInfoparam {

    @ApiModelProperty(value = "新增角色名称")
    private String rolesname;

    @ApiModelProperty(value = "描述内容")
    private String descriptor;

    @ApiModelProperty(value = "权限long数组")
    private List<Long> authority;

}
