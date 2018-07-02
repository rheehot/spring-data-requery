package org.springframework.data.requery.repository.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.data.repository.config.DefaultRepositoryBaseClass
import org.springframework.data.repository.query.QueryLookupStrategy
import org.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean
import java.lang.annotation.Inherited
import kotlin.reflect.KClass

/**
 * org.springframework.data.requery.repository.config.EnableRequeryRepositories
 *
 * @author debop
 */

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Inherited
@Import(RequeryRepositoriesRegistar::class)
annotation class EnableRequeryRepositories(

    vararg val value: String = [],

    val basePackages: Array<String> = [],

    val basePackageClasses: Array<KClass<*>> = [],

    val includeFilters: Array<ComponentScan.Filter> = [],

    val excludeFilters: Array<ComponentScan.Filter> = [],

    val repositoryImplementationPostfix: String = "Impl",

    // Not supported 
    //    val namedQueriesLocation: String = "",

    val queryLookupStrategy: QueryLookupStrategy.Key = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND,

    val repositoryFactoryBeanClass: KClass<*> = RequeryRepositoryFactoryBean::class,

    val repositoryBaseClass: KClass<*> = DefaultRepositoryBaseClass::class,

    val transactionManagerRef: String = "transactionManager",

    val considerNestedRepositories: Boolean = false,

    val enableDefaultTransactions: Boolean = true
)
