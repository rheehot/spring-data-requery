package com.coupang.springframework.data.requery.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * NamedQueries
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NamedQueries {

    NamedQuery[] value();
}
