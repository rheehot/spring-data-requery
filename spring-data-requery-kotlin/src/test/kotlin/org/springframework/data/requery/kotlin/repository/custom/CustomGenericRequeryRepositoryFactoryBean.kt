package org.springframework.data.requery.kotlin.repository.custom

import org.springframework.data.requery.kotlin.core.RequeryOperations
import org.springframework.data.requery.kotlin.repository.RequeryRepository
import org.springframework.data.requery.kotlin.repository.support.RequeryRepositoryFactory
import org.springframework.data.requery.kotlin.repository.support.RequeryRepositoryFactoryBean

/**
 * org.springframework.data.requery.repository.custom.CustomGenericRequeryRepositoryFactoryBean
 *
 * @author debop
 */
class CustomGenericRequeryRepositoryFactoryBean<T: RequeryRepository<Any, Any>>(repositoryInterface: Class<out T>)
    : RequeryRepositoryFactoryBean<T, Any, Any>(repositoryInterface) {

    override fun createRepositoryFactory(operations: RequeryOperations): RequeryRepositoryFactory {
        return CustomGenericRequeryRepositoryFactory(operations)
    }
}