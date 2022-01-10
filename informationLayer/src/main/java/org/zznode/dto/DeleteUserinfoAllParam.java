package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "DeleteUserinfoAllParam")
public class DeleteUserinfoAllParam {

    @ApiModelProperty(value = "用户账号long数组")
    private String usersid;

}
