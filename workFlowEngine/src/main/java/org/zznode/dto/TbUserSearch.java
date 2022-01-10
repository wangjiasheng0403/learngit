package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TbUserSearch")
public class TbUserSearch {
    @ApiModelProperty(value = "当前页", example = "1")
    private Long pageNo;
    @ApiModelProperty(value = "分页大小", example = "10")
    private Long pageSize;
    @ApiModelProperty(hidden = true)
    private String token;
    @ApiModelProperty(value = "账号")
    private String account;
    @ApiModelProperty(value = "姓名")
    private String username;
    @ApiModelProperty(value = "电话")
    private String phone;
    @ApiModelProperty(value = "党组织id")
    private Long paryId;
    @ApiModelProperty(value = "1：启用，0：禁用")
    private String accountStatus;
}
