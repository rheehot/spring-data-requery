package org.springframework.data.requery.kotlin.annotation

import io.requery.sql.EntityDataStore

/**
 * NamedQuery
 * NOTE: 현재 지원하지는 않지만, 향후 지원을 위해 만들어 놓음
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
