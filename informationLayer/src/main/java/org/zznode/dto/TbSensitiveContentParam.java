package org.zznode.dto;


import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author bh
 */
@Data
public class TbSensitiveContentParam {

    @NotBlank
    private String content;
}
