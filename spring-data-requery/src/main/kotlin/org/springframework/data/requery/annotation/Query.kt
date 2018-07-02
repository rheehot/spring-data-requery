package org.springframework.data.requery.annotation

import org.springframework.data.annotation.QueryAnnotation

/**
 * Query
 *
 * @author debop@coupang.com
 * @since 18. 7. 2
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@QueryAnnotation
@MustBeDocumented
annotation class Query(
    /**
     * Defines the SQL query to be executed when the annotated queryMethod is called.
     */
    val value: String = "",
    /**
     * Defines a special count query that shall be used for pagination queries to lookup the total number of elements for
     * a page. If non is configured we will derive the count query from the queryMethod name.
     */
    val countQuery: String = ""

)