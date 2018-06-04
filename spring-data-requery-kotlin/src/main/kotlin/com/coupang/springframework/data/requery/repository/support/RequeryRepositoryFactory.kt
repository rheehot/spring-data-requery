package com.coupang.springframework.data.requery.repository.support

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.core.RequeryOperations
import com.coupang.springframework.data.requery.repository.RequeryRepository
import org.springframework.data.repository.core.RepositoryInformation
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import java.io.Serializable

/**
 * RequeryRepositoryFactory
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
class RequeryRepositoryFactory(private val requeryOperations: RequeryOperations): RepositoryFactorySupport() {

    companion object: KLogging()

    // TODO: 구현 중
    // private val context = requeryOperations.converter.mappingContext

    @SuppressWarnings("unchecked")
    override fun <T, ID> getEntityInformation(domainClass: Class<T>): RequeryEntityInformation<T, ID> {
        TODO("구현 중")
        // return RequeryPersistentEntityInformation<T, ID>(domainClass)
    }

    @SuppressWarnings("rawtypes", "unchecked")
    override fun getTargetRepository(metadata: RepositoryInformation): RequeryRepository<*, *> {
        TODO("구현 중")
        // return SimpleRequeryRepository(operations = requeryOperations, entityType = metadata.domainType)
    }

    override fun getRepositoryBaseClass(metadata: RepositoryMetadata): Class<*> {
        return SimpleRequeryRepository::class.java
    }

    protected fun getTargetRepository(information: RepositoryInformation,
                                      requeryOperations: RequeryOperations): RequeryRepositoryImplementation<*, *> {

        log.debug { "Get target repository. information=$information" }

        val entityInformation: RequeryEntityInformation<*, Serializable> = getEntityInformation(information.domainType)
        val repository: Any = getTargetRepositoryViaReflection(information, entityInformation, requeryOperations)

        check(repository is RequeryRepositoryImplementation<*, *>) {
            "requery is not RequeryRepositoryImplementation type."
        }

        return repository as RequeryRepositoryImplementation<*, *>
    }
}