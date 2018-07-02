package org.springframework.data.requery.annotation

import io.requery.sql.EntityDataStore

/**
 * NamedQuery
 *
 * @author debop@coupang.com
 * @since 18. 7. 2
 */
@Repeatable
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
annotation class NamedQuery(
    /**
     * (Required) The name used to refer to the query with the [EntityDataStore]
     * methods that create query objects.
     */
    val name: String,
    /**
     * (Required)
     * The query string in the Plain SQL language.
     */
    val query: String
)
