package org.springframework.data.requery.repository.support

import mu.KotlinLogging
import org.springframework.data.mapping.context.MappingContext
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport
import org.springframework.data.requery.core.RequeryOperations

/**
 * Special adapter for Springs [org.springframework.beans.factory.FactoryBean] interface to allow easy setup of
 * repository factories via Spring configuration.
 *
 * @author debop
 */
open class RequeryRepositoryFactoryBean<T: Repository<E, ID>, E, ID>(repositoryInterface: Class<out T>)
    : TransactionalRepositoryFactoryBeanSupport<T, E, ID>(repositoryInterface) {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    private var operations: RequeryOperations? = null

    fun setOperations(operations: RequeryOperations) {
        this.operations = operations
    }

    open override fun setMappingContext(mappingContext: MappingContext<*, *>) {
        super.setMappingContext(mappingContext)
    }

    open override fun doCreateRepositoryFactory(): RepositoryFactorySupport {
        require(operations != null) { "RequeryOperations must not be null!" }
        return createRepositoryFactory(operations!!)
    }

    protected open fun createRepositoryFactory(operations: RequeryOperations): RequeryRepositoryFactory {
        return RequeryRepositoryFactory(operations)
    }

    open override fun afterPropertiesSet() {
        require(operations != null) { "RequeryOperations must not be null!" }

        log.debug { "Before afterPropertiesSet" }
        try {
            super.afterPropertiesSet()
        } catch(e: Exception) {
            log.error(e) { "Fail to run afterPropertiesSet" }
        }
        log.debug { "After afterPropertiesSet" }
    }
}