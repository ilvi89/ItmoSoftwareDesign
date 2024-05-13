package ru.abolsoft.sseconnect.infr.adapter.utils.retry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RetryWithBackoff {
    int maxAttempts() default 3;

    long delay() default 1000; // in milliseconds

    double multiplier() default 1.5;

    Class<?> handlerClass() default RetryHandler.class;
}

