package org.springframework.data.requery.annotation

/**
 * NamedQueries
 *
 * @author debop@coupang.com
 * @since 18. 7. 2
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class NamedQueries(

    val value: Array<NamedQuery>

)