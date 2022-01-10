package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel(value = "RoleLogParam")
public class RoleLogParam {

    /**
     * 缺省主键
     */
    @NotNull
    @ApiModelProperty(value = "id", required = true)
    private String id;

    /**
     * 用户ID
     */
    @NotNull
    @ApiModelProperty(value = "用户ID", required = true)
    private String userId;

    /**
     * 资源编号
     */
    @NotBlank
    @ApiModelProperty(value = "资源编号", required = true)
    private String dataId;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    private String make;

    /**
     * 是否点赞
     */
    @ApiModelProperty(value = "是否点赞")
    private String like;

    /**
     * 打分
     */
    @ApiModelProperty(value = "打分")
    private BigDecimal collect;

    @ApiModelProperty(value = "终端类型")
    private String terminalType;
}
