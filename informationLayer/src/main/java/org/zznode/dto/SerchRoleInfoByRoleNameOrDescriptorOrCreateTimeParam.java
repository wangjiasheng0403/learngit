package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

@Data
@ApiModel(value = "SerchRoleInfoByRoleNameOrDescriptorOrCreateTimeParam")
public class SerchRoleInfoByRoleNameOrDescriptorOrCreateTimeParam {

    @ApiModelProperty(value = "角色名称")
    private String rolesname;

    @ApiModelProperty(value = "描述内容")
    private String descriptor;

    @ApiModelProperty(value = "查询开始时间")
    private String startTime;

    @ApiModelProperty(value = "查询结束时间")
    private String endTime;

    @ApiModelProperty(value = "分页页面下标，分页从1开始")
    private Integer pageIndex;

    @ApiModelProperty(value = "每页的记录数")
    private Integer pageSize;
}
