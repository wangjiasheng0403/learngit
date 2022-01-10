package org.zznode.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryDateParam {


    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

}
