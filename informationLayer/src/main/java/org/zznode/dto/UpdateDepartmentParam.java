package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "UpdateDepartmentParam")
public class UpdateDepartmentParam {

    @ApiModelProperty(value = "部门ID")
    private Long departid;

    @ApiModelProperty(value = "部门名称")
    private String departname;

    @ApiModelProperty(value = "上级部门ID")
    private Long superiordepartid;

    @ApiModelProperty(value = "所属组织ID")
    private Long partid;

    @ApiModelProperty(value = "部门ID数组")
    private List<Long> departmentsid;

}
