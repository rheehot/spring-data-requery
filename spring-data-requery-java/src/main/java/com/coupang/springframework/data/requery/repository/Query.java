package com.coupang.springframework.data.requery.repository;

import org.springframework.data.annotation.QueryAnnotation;

import java.lang.annotation.*;

/**
 * com.coupang.springframework.data.requery.repository.Query
 *
 * @author debop
 * @since 18. 6. 6
 */
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@QueryAnnotation
@Documented
public @interface Query {

    String value() default "";

    String countQuery() default "";

    String name() default "";

    String countName() default "";
}
