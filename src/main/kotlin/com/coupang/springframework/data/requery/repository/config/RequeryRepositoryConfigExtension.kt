package com.coupang.springframework.data.requery.repository.config

import com.coupang.kotlinx.logging.KLogging
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


    override fun getModulePrefix(): String {
        TODO("not implemented")
    }

    override fun getRepositoryFactoryBeanClassName(): String {
        TODO("not implemented")
    }
}