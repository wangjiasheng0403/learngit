package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SerchDictByAreaParam")
public class SerchDictByAreaParam {

    @ApiModelProperty(value = "需要查询的数据域")
    private Integer area;

}
