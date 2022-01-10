package org.zznode.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Chris
 */
@Data
public class UsersScoreParam {

    private String partName;
    /**
     * 统计时段
     */
    private String timeQuantum;
    /**
     * 当前页
     */
    @NotNull
    @ApiModelProperty(value = "当前页", example = "1", required = true)
    private Integer pageNo;

    /**
     * 分页大小
     */
    @NotNull
    @ApiModelProperty(value = "分页大小", example = "10", required = true)
    private Integer pageSize;
}
