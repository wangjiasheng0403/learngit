package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 党组织机构查询参数类
 *
 * @author bh
 */
@Data
@ApiModel(value = "ListOrganizationParam")
public class ListOrganizationParam {

    @ApiModelProperty(hidden = true)
    private String token;

    @ApiModelProperty(value = "党组织名")
    private String organizationName;

    @ApiModelProperty(value = "开始时间")
    private String startDate;

    @ApiModelProperty(value = "结束时间")
    private String endDate;

    @ApiModelProperty(value = "当前页", example = "1")
    private Long pageNo;

    @ApiModelProperty(value = "分页大小", example = "10")
    private Long pageSize;

    @ApiModelProperty(value = "userId")
    private Long userId;
}
