package org.zznode.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel(value = "InsertDataResourceInfoParam")
public class InsertDataResourceInfoParam {

    @NotBlank
    @ApiModelProperty(value = "场景编号")
    private String dataid;

    @ApiModelProperty(value = "场景描述")
    private String datainfo;

    @ApiModelProperty(value = "场景所属党组织")
    private String databelong;

    @ApiModelProperty(value = "场景类型，公共场景管理请传入0，红色资源请传入1，党组织资源发布管理请传入2")
    private Integer datatype;

    @ApiModelProperty(value = "场景名称")
    private String dataname;

    @ApiModelProperty(value = "showType 在公共模版和红色基地两个功能中，1是启动用，2是禁用。在党建资源发布管理功能中，可以设置showtype为1，1表示公开的组织场景，showtype为2表示是私有资源。")
    private String showtype;

    @ApiModelProperty(value = "场景存放路径")
    private String datapath;

    @ApiModelProperty(value = "场景模版ID")
    private String basemodelid;

    @ApiModelProperty(value = "场景缩略图存放路径")
    private String imgpath;

    @ApiModelProperty(value = "编辑人")
    private String editor;

}
