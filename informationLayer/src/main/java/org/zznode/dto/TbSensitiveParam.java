package org.zznode.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author bh
 */
@Data
public class TbSensitiveParam {

    /**
     * 敏感字段
     */
    private String sensitivity;

    @NotBlank
    private String token;


}
