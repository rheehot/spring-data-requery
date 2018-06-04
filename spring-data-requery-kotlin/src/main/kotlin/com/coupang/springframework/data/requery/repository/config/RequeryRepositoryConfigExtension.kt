package com.coupang.springframework.data.requery.repository.config

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.repository.RequeryRepository
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport

/**
 * RequeryRepositoryConfigExtension
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
class RequeryRepositoryConfigExtension: RepositoryConfigurationExtensionSupport() {

    companion object: KLogging() {
        const val DEFAULT_TRANSACTION_MANAGER_BEAN_NAME = "transactionManager"
        const val ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE = "enableDefaultTransactions"
    }

    override fun getModuleName(): String {
        return "Requery"
    }

    override fun getModulePrefix(): String {
        return "requery"
    }

    override fun getRepositoryFactoryBeanClassName(): String {
        return RequeryRepositoryFactoryBean::class.java.name
    }

    override fun getIdentifyingAnnotations(): MutableCollection<Class<out Annotation>> {
        return mutableListOf(io.requery.Entity::class.java)
    }

    override fun getIdentifyingTypes(): MutableCollection<Class<*>> {
        return mutableListOf(RequeryRepository::class.java)
    }
}