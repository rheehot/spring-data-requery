package com.coupang.springframework.data.requery.annotation;

import java.lang.annotation.*;

/**
 * Indicates a method should be regarded as modifying query.
 *
 * @author debop
 * @since 18. 6. 7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Documented
public @interface Modifying {

    /**
     * Defines whether we should flush the underlying persistence context before executing the modifying query.
     *
     * @return
     */
    boolean flushAutomatically() default false;

    /**
     * Defines whether we should clear the underlying persistence context after executing the modifying query.
     *
     * @return
     */
    boolean clearAutomatically() default false;
}
