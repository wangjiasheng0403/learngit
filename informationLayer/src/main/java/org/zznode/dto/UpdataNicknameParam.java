package org.zznode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "UpdataNicknameParam")
public class UpdataNicknameParam {
    @NotNull
    @ApiModelProperty(value = "用户ID")
    private Long userid;

    @NotBlank
    @ApiModelProperty(value = "用户昵称")
    private String nickname;
}
