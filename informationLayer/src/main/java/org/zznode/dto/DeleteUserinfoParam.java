package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DeleteUserinfoParam")
public class DeleteUserinfoParam {

    @ApiModelProperty(value = "用户ID")
    private Long usersid;
}
