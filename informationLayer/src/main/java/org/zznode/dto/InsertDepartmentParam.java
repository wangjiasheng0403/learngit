package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "InsertDepartmentParam")
public class InsertDepartmentParam {

    @ApiModelProperty(value = "部门名称")
    private String departname;

    @ApiModelProperty(value = "上级部门ID")
    private Long fatherid;

    @ApiModelProperty(value = "所属组织ID")
    private Long partid;
}
