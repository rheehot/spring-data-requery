package com.coupang.springframework.data.requery.repository.config

import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import java.lang.annotation.Inherited
import kotlin.reflect.KClass

/**
 * EnableRequeryRepositories
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Inherited
@Import(RequeryRepositoriesRegistrar::class)
annotation class EnableRequeryRepositories @JvmOverloads constructor(
    val value: Array<String> = [],
    val basePackages: Array<String> = [],
    val basePackageClasses: Array<KClass<*>> = [],
    val includeFilters: Array<ComponentScan.Filter> = [],
    val excludeFilters: Array<ComponentScan.Filter> = [],
    val repositoryImplementationPostfix: String = "Impl",
    val repositoryFactoryBeanClass: KClass<*> = RequeryRepositoryFactoryBean::class)