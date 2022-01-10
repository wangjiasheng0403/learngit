package org.zznode.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class ScenesFlowBaseInfoParam {

    @NotBlank
    private String token;
}
