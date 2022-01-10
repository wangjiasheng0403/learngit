package org.zznode.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class RoleLogParam {

    /**
     * 缺省主键
     */
    @NotNull
    private Long id;

    /**
     * 用户ID
     */
    @NotNull
    private Long userId;

    /**
     * 资源编号
     */
    @NotBlank
    private String dataId;

    /**
     * 访问时间
     */
    @JsonProperty("cTime")
    private String cTime;

    /**
     * 评论内容
     */
    private String make;

    /**
     * 是否点赞
     */
    private String like;

    /**
     * 打分
     */
    private Integer collect;
}
