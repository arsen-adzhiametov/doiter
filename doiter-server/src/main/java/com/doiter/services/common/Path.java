package com.doiter.services.common;

import java.lang.annotation.ElementType;

/**
 * User: Artur
 */
@java.lang.annotation.Target({ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Path {
    String value();
}
