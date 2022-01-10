package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "AuthorizeDataWithDepartIDParam")
public class AuthorizeDataWithDepartIDParam {

    @NotNull
    @ApiModelProperty(value = "部门ID")
    private Long departID;

    @ApiModelProperty(value = "当前第几页")
    private Integer pageIndex;

    @ApiModelProperty(value = "每页几条记录")
    private Integer pageSize;
}
