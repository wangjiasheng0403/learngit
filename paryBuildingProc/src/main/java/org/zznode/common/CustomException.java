package org.zznode.common;

import lombok.Getter;

public class CustomException extends RuntimeException {
    @Getter
    private final String code;
    @Getter
    private final String description;
    @Getter
    private final String result;

    public CustomException(String code, String result, String description) {
        super();
        this.code = code;
        this.result = result;
        this.description = description;
    }
}
