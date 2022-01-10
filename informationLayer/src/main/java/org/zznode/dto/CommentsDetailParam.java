package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel(value = "CommentsDetailParam")
public class CommentsDetailParam extends QueryDateParam {

    @NotBlank
    @ApiModelProperty(value = "昵称", required = true)
    private String nickname;

    @NotBlank
    @ApiModelProperty(value = "场景id", required = true)
    private String dataId;
}
