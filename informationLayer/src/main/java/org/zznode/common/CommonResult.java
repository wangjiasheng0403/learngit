package org.zznode.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 通用结果返回
 */
@Data
public class CommonResult<T> {

    /**
     * code状态码, 000000表示成功
     */
    private String code;

    /**
     * 响应数据
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private T data;

    /**
     * 调用结果
     */
    private String description;
    /**
     * 返回值,“success”表示成功，其他信息根据状态码代表的不同含义返回不同的信息。
     */
    private String result;

    protected CommonResult(String code, String result, String description, T data) {
        this.code = code;
        this.result = result;
        this.description = description;
        this.data = data;
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>("000000", "success", "成功", data);
    }

    public static <T> CommonResult<T> success() {
        return new CommonResult<T>("000000", "操作成功", "成功", null);
    }

    public static <T> CommonResult<T> error(CustomException e) {
        return new CommonResult<>(e.getCode(), e.getResult(), e.getDescription(), null);
    }

}
