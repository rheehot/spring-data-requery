package org.springframework.data.requery.repository.support

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
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
class RequeryRepositoryFactoryBean<T: Repository<E, ID>, E, ID>(repositoryInterface: Class<out T>)
    : TransactionalRepositoryFactoryBeanSupport<T, E, ID>(repositoryInterface) {

    private val log = KotlinLogging.logger { }

    private var operations: RequeryOperations? = null
        get() = field
        @Autowired(required = false) set(value) {
            field = value
        }

    override fun setMappingContext(mappingContext: MappingContext<*, *>) {
        super.setMappingContext(mappingContext)
    }

    override fun doCreateRepositoryFactory(): RepositoryFactorySupport {
        TODO("not implemented")
    }

    protected fun createRepositoryFactory(operations: RequeryOperations): RequeryRepositoryFactory {
        return RequeryRepositoryFactory(operations)
    }

    override fun afterPropertiesSet() {
        require(operations != null) { "RequeryOperations must not be null!" }

        log.debug { "Before afterPropertiesSet" }
        try {
            super.afterPropertiesSet()
        } catch(e: Exception) {
            log.debug(e) { "Fail to run afterPropertiesSet" }
        }
        log.debug { "After afterPropertiesSet" }
    }
}