package com.coupang.springframework.data.requery.repository.support

import com.coupang.kotlinx.logging.KLogging
import io.requery.Persistable
import io.requery.sql.EntityDataStore
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport

/**
 * RequeryRepositoryFactoryBean
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
class RequeryRepositoryFactoryBean<T: Repository<S, ID>, S, ID>(repositoryInterface: Class<out T>)
    : TransactionalRepositoryFactoryBeanSupport<T, S, ID>(repositoryInterface) {

    companion object: KLogging()

    var entityDataStore: EntityDataStore<Persistable>? = null

    override fun doCreateRepositoryFactory(): RepositoryFactorySupport {
        check(entityDataStore != null) { "EntityDataStore must not be null!" }
        return createRepositoryFactory(entityDataStore!!)
    }

    protected fun createRepositoryFactory(entityDataStore: EntityDataStore<Persistable>): RepositoryFactorySupport {
        return RequeryRepositoryFactory(entityDataStore)
    }

    override fun afterPropertiesSet() {
        check(entityDataStore != null) { "EntityDataStore must not be null!" }
        super.afterPropertiesSet()
    }

    override fun setBeanFactory(beanFactory: BeanFactory) {
        assert(beanFactory is ListableBeanFactory) { "beanFactory must be ListableBeanFactory" }
        super.setBeanFactory(beanFactory)
    }
}