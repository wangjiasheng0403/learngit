package org.zznode.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Chris
 */
@Data
public class SensitiveHistoryParam {

    /**
     * 检查类型: 1场景素材，2评论内容
     */
    @ApiModelProperty(value = "检查类型: 1场景素材，2评论内容")
    private Short checktype;

    /**
     * 涉敏内容
     */
    @ApiModelProperty(value = "涉敏内容")
    private String sensitivecontent;

    /**
     * 检查时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startDate;

    @ApiModelProperty(value = "结束时间")
    private String endDate;

    @ApiModelProperty(value = "当前页", example = "1")
    private Long pageNo;

    @ApiModelProperty(value = "分页大小", example = "10")
    private Long pageSize;

}

