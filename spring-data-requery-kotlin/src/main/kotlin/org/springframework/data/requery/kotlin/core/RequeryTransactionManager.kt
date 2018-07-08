package org.springframework.data.requery.kotlin.core

import io.requery.TransactionIsolation
import io.requery.sql.EntityDataStore
import mu.KotlinLogging
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.DefaultTransactionStatus
import java.sql.Connection
import javax.sql.DataSource

/**
 * RequeryTransactionManager
 *
 * @author debop@coupang.com
 * @since 18. 7. 2
 */
class RequeryTransactionManager(val dataStore: EntityDataStore<Any>,
                                dataSource: DataSource): DataSourceTransactionManager(dataSource) {

    private val log = KotlinLogging.logger { }

    override fun doBegin(transaction: Any, definition: TransactionDefinition) {
        super.doBegin(transaction, definition)

        log.debug { "Begin tranaction. definition=$definition" }

        val isolation = transactionIsolationOf(definition.isolationLevel)

        isolation?.let {
            dataStore.transaction().begin(isolation)
        } ?: dataStore.transaction().begin()
    }

    override fun doCommit(status: DefaultTransactionStatus) {
        if(dataStore.transaction().active()) {
            try {
                log.debug { "Commit requery transaction..." }
                dataStore.transaction().commit()
            } catch(ignored: Throwable) {
                log.trace(ignored) { "Fail to commit in requery transaction" }
            }
        }
        super.doCommit(status)
    }

    override fun doRollback(status: DefaultTransactionStatus) {
        if(dataStore.transaction().active()) {
            try {
                log.debug { "Rollback requery transaction..." }
                dataStore.transaction().rollback()
            } catch(ignored: Throwable) {
                log.trace(ignored) { "Fail to rollback in requery transaction" }
            }
        }
        super.doRollback(status)
    }

    override fun doSuspend(transaction: Any): Any {
        log.trace { "Suspend ... transaction=$transaction" }
        return super.doSuspend(transaction)
    }

    override fun doResume(transaction: Any?, suspendedResources: Any) {
        log.trace { "Resume ... transaction=$transaction, suspendedResources=$suspendedResources" }
        super.doResume(transaction, suspendedResources)
    }

    private fun transactionIsolationOf(isolationLevel: Int): TransactionIsolation? = when(isolationLevel) {
        Connection.TRANSACTION_NONE -> TransactionIsolation.NONE
        Connection.TRANSACTION_READ_UNCOMMITTED -> TransactionIsolation.READ_UNCOMMITTED
        Connection.TRANSACTION_READ_COMMITTED -> TransactionIsolation.READ_COMMITTED
        Connection.TRANSACTION_REPEATABLE_READ -> TransactionIsolation.REPEATABLE_READ
        Connection.TRANSACTION_SERIALIZABLE -> TransactionIsolation.SERIALIZABLE
        else -> null
    }
}