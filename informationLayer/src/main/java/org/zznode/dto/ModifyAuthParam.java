package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "ModifyAuthParam")
public class ModifyAuthParam {

    @NotNull
    @ApiModelProperty(value = "组织关系类型;0为本系统，1为共建部门")
    private Integer relationType;

    @NotNull
    @ApiModelProperty(value = "当前组织id")
    private String paryId;

    @NotNull
    @ApiModelProperty(value = "被授权组织id  type: Array")
    private String visitParyIds;
}
