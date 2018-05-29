package com.coupang.springframework.data.requery.repository.support

import io.requery.Persistable
import io.requery.sql.EntityDataStore
import org.springframework.data.repository.core.EntityInformation
import org.springframework.data.repository.core.RepositoryInformation
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.data.repository.core.support.RepositoryFactorySupport

/**
 * RequeryRepositoryFactory
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
open class RequeryRepositoryFactory(private val entityDataStore: EntityDataStore<Persistable>): RepositoryFactorySupport() {


    open override fun <T: Any?, ID: Any?> getEntityInformation(domainClass: Class<T>): EntityInformation<T, ID> {
        TODO("not implemented")
    }

    open override fun getTargetRepository(metadata: RepositoryInformation): RequeryRepositoryImplementation<*, *> {
        TODO("not implemented")
    }

    open override fun getRepositoryBaseClass(metadata: RepositoryMetadata): Class<*> {
        TODO("not implemented")
    }
}