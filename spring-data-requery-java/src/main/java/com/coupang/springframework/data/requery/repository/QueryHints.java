package com.coupang.springframework.data.requery.repository;

import javax.persistence.QueryHint;
import java.lang.annotation.*;

/**
 * com.coupang.springframework.data.requery.repository.QueryHints
 *
 * @author debop
 * @since 18. 6. 6
 */
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryHints {

    QueryHint[] value() default {};

    boolean forCounting() default true;
}
