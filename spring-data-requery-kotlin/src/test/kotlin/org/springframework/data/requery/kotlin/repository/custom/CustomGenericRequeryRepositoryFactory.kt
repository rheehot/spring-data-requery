package org.springframework.data.requery.repository.custom

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.springframework.data.repository.core.RepositoryInformation
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.data.requery.kotlin.core.RequeryOperations
import org.springframework.data.requery.repository.support.RequeryEntityInformation
import org.springframework.data.requery.repository.support.RequeryRepositoryFactory
import org.springframework.data.requery.repository.support.SimpleRequeryRepository

/**
 * org.springframework.data.requery.repository.custom.CustomGenericRequeryRepositoryFactory
 *
 * @author debop
 */
class CustomGenericRequeryRepositoryFactory(operations: RequeryOperations): RequeryRepositoryFactory(operations) {

    override fun getTargetRepository(metadata: RepositoryInformation): SimpleRequeryRepository<out Any, *> {

        val entityMetadata = mock<RequeryEntityInformation<out Any, out Any>>()
        whenever(entityMetadata.javaType).doReturn(metadata.domainType)

        return CustomGenericRequeryRepository(entityMetadata, operations)
    }

    override fun getRepositoryBaseClass(metadata: RepositoryMetadata): Class<*> {
        return CustomGenericRequeryRepository::class.java
    }
}