package org.zznode.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UsersOptionParam")
public class UsersOptionParam {

    @ApiModelProperty(value = "申请人用户ID")
    private Long userId;

    @ApiModelProperty(value = "被修改人用户ID")
    private Long modifUserId;

    @ApiModelProperty(value = "操作类型;定义增删改动作")
    private String optionType;

    @ApiModelProperty(value = "操作菜单;操作发生在后台具体菜单名")
    private String optionByMenu;

    @ApiModelProperty(value = "操作字段;操作发生在菜单的字段")
    private String optionByField;

    @ApiModelProperty(value = "操作内容;操作具体内容（如修改字段值，记录修改后值的内容）")
    private String optionInfo;

}
