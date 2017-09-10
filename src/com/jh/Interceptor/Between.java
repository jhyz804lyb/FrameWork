package com.jh.Interceptor;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Between {
    String startField() default "startTime";

    String endField() default "endTime";

}
