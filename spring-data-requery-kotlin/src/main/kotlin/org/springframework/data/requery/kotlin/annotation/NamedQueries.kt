package org.springframework.data.requery.kotlin.annotation

/**
 * NamedQueries
 *
 * NOTE: 현재 지원하지는 않지만, 향후 지원을 위해 만들어 놓음
 *
 * @author debop@coupang.com
 * @since 18. 7. 2
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class NamedQueries(

    val value: Array<NamedQuery>

)