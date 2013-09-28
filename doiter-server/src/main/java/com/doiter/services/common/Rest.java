package com.doiter.services.common;

import org.springframework.stereotype.Component;

/**
 * @author : Artur
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Component
public @interface Rest {
    String value();
}
