package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TbUserSearch")
public class TbUserDeleteParam {
    @ApiModelProperty(value = "userId")
    private Long userId;
    @ApiModelProperty(hidden = true)
    private String token;
    @ApiModelProperty(value = "deleteUserId")
    private Long deleteUserId;
}
