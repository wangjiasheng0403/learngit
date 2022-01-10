package org.zznode.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LogCompar {
    /**
     * 汉字全称
     * @return
     */
    String value();
}