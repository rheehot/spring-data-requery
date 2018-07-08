package org.springframework.data.requery.annotation;

import io.requery.sql.EntityDataStore;

import java.lang.annotation.*;

/**
 * NamedQuery
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@Repeatable(NamedQueries.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NamedQuery {

    /**
     * (Required) The name used to refer to the query with the {@link EntityDataStore}
     * methods that create query objects.
     */
    String name();

    /**
     * (Required)
     * The query string in the Plain SQL language.
     */
    String query();

}
