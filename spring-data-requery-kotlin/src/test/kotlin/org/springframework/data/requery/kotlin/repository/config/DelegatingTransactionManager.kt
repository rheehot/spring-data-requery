package org.springframework.data.requery.kotlin.repository.config

import mu.KotlinLogging
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus

/**
 * org.springframework.data.requery.repository.config.DelegatingTransactionManager
 *
 * @author debop
 */
class DelegatingTransactionManager(private val txManager: PlatformTransactionManager): PlatformTransactionManager {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    var transactionRequests: Int = 0
    var definition: TransactionDefinition? = null

    override fun getTransaction(definition: TransactionDefinition?): TransactionStatus {
        this.transactionRequests++
        this.definition = definition

        log.info("Get transaction. transactionRequests={}, definition={}", transactionRequests, definition)

        return txManager.getTransaction(definition)
    }

    override fun commit(status: TransactionStatus) {
        log.info("Commit transaction. status={}", status)
        txManager.commit(status)
    }

    override fun rollback(status: TransactionStatus) {
        log.info("Rollback transaction. status={}", status)
        txManager.rollback(status)
    }

    fun resetCount() {
        log.info("Reset transaction request.")
        this.transactionRequests = 0
        this.definition = null
    }
}