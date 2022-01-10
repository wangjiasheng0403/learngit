package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "GetOrgAuthTreeParam")
public class GetOrgAuthTreeParam {


    @NotNull
    @ApiModelProperty(value = "组织关系类型;0为本系统，1为共建部门")
    private Integer relationType;

    @ApiModelProperty(hidden = true)
    private String token;
}
