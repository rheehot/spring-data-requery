package org.springframework.data.requery.repository.support

import mu.KotlinLogging
import org.springframework.data.domain.Persistable
import org.springframework.data.repository.core.EntityInformation
import org.springframework.data.repository.core.RepositoryInformation
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import org.springframework.data.repository.query.EvaluationContextProvider
import org.springframework.data.repository.query.QueryLookupStrategy
import org.springframework.data.requery.core.RequeryOperations
import org.springframework.data.requery.provider.RequeryPersistenceProvider
import org.springframework.data.requery.repository.query.RequeryQueryLookupStrategy
import java.util.*

/**
 * Requery specific generic repository factory.
 *
 * @author debop
 */
class RequeryRepositoryFactory(val operations: RequeryOperations): RepositoryFactorySupport() {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    private val extractor = RequeryPersistenceProvider(operations)
    private val crudMethodMetadataPostProcessor = CrudMethodMetadataPostProcessor()

    override fun setBeanClassLoader(classLoader: ClassLoader) {
        super.setBeanClassLoader(classLoader)
        this.crudMethodMetadataPostProcessor.setBeanClassLoader(classLoader)
    }

    override fun getTargetRepository(metadata: RepositoryInformation): SimpleRequeryRepository<out Any, *> {
        val repository = getTargetRepository(metadata, operations)
        repository.setRepositoryMethodMetadata(crudMethodMetadataPostProcessor.getCrudMethodMetadata())
        return repository
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getTargetRepository(information: RepositoryInformation,
                                      operations: RequeryOperations): SimpleRequeryRepository<out Any, *> {
        log.debug { "Get target repository. information=$information" }

        val entityInformation = getEntityInformation<Any, Any>(information.domainType as Class<Any>)

        return getTargetRepositoryViaReflection(information, entityInformation, operations)
    }

    override fun getRepositoryBaseClass(metadata: RepositoryMetadata): Class<*> = SimpleRequeryRepository::class.java


    override fun getQueryLookupStrategy(key: QueryLookupStrategy.Key?,
                                        evaluationContextProvider: EvaluationContextProvider): Optional<QueryLookupStrategy> {
        log.debug { "Create QueryLookupStrategy by key=$key" }
        return Optional.ofNullable(RequeryQueryLookupStrategy.create(operations, key, evaluationContextProvider))
    }

    @Suppress("UNCHECKED_CAST")
    override fun <E: Any, ID: Any> getEntityInformation(domainClass: Class<E>): EntityInformation<E, ID> {

        return RequeryEntityInformationSupport.getEntityInformation(domainClass as Class<Persistable<ID>>,
                                                                    operations)
            as RequeryEntityInformationSupport<E, ID>
    }


}