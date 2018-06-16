package com.coupang.springframework.data.requery.annotation;

import org.springframework.data.annotation.QueryAnnotation;

import java.lang.annotation.*;

/**
 * com.coupang.springframework.data.requery.annotation.Query
 *
 * @author debop
 * @since 18. 6. 6
 */
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@QueryAnnotation
@Documented
public @interface Query {

    /**
     * Defines the SQL query to be executed when the annotated method is called.
     */
    String value() default "";

    /**
     * Defines a special count query that shall be used for pagination queries to lookup the total number of elements for
     * a page. If non is configured we will derive the count query from the method name.
     */
    String countQuery() default "";

    /**
     * Defines the projection part of the count query that is generated for pagination. If neither {@link #countQuery()}
     * not {@link #countProjection()} is configured we will derive the count query from the method name.
     */
    String countProjection() default "";

}