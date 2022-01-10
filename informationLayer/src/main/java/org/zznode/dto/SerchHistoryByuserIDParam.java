package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SerchHistoryByuserIDParam")
public class SerchHistoryByuserIDParam {

    @ApiModelProperty(value = "用户ID")
    private Long userid;
}
