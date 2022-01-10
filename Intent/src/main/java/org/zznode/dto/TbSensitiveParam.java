package org.zznode.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author bh
 */
@Data
public class TbSensitiveParam {

    @ApiModelProperty(hidden = true)
    private String token;
    /**
     * 敏感字段
     */
    private String sensitivity;


}
