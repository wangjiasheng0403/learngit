package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "AuthorizeOpenDataParam")
public class AuthorizeOpenDataParam {

    @NotNull
    @ApiModelProperty(value = "当前第几页")
    private Integer pageIndex;

    @NotNull
    @ApiModelProperty(value = "每页几条记录")
    private Integer pageSize;

}
