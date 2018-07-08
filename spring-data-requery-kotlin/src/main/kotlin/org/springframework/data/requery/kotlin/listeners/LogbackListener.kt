package org.springframework.data.requery.kotlin.listeners

import io.requery.sql.BoundParameters
import io.requery.sql.EntityStateListener
import io.requery.sql.StatementListener
import mu.KLogging
import java.sql.Statement

/**
 * LogbackListener
 *
 * @author debop@coupang.com
 * @since 18. 7. 2
 */
class LogbackListener<T> @JvmOverloads constructor(val printStatement: Boolean = false)
    : EntityStateListener<T>, StatementListener {

    companion object: KLogging()

    override fun preInsert(entity: T) {
        logger.debug { "preInsert $entity" }
    }

    override fun preUpdate(entity: T) {
        logger.debug { "preUpdate $entity" }
    }

    override fun preDelete(entity: T) {
        logger.debug { "preDelete $entity" }
    }

    override fun postDelete(entity: T) {
        logger.debug { "postDelete $entity" }
    }

    override fun postInsert(entity: T) {
        logger.debug { "postInsert $entity" }
    }

    override fun postUpdate(entity: T) {
        logger.debug { "postUpdate $entity" }
    }

    override fun postLoad(entity: T) {
        logger.debug { "postLoad $entity" }
    }

    override fun afterExecuteUpdate(statement: Statement?, count: Int) {
        logger.debug { "afterExecuteUpdate count=[$count]" }
    }

    override fun beforeExecuteBatchUpdate(statement: Statement?, sql: String?) {
        statement?.let {
            logger.debug { "beforeExecuteBatchUpdate SQL:\n$sql" }
            if(printStatement)
                logger.debug { "$statement" }
        }
    }

    override fun afterExecuteQuery(statement: Statement?) {
        logger.debug { "afterExecuteQuery" }
    }

    override fun afterExecuteBatchUpdate(statement: Statement?, count: IntArray?) {
        logger.debug { "afterExecuteBatchUpdate count=[$count]" }
    }

    override fun beforeExecuteUpdate(statement: Statement?, sql: String?, parameters: BoundParameters?) {
        if(parameters != null && !parameters.isEmpty) {
            logger.debug { "beforeExecuteUpdate SQL:\n$sql ($parameters)" }
        } else {
            logger.debug { "beforeExecuteUpdate SQL:\n$sql" }
        }

        if(printStatement)
            logger.debug { "$statement" }
    }

    override fun beforeExecuteQuery(statement: Statement?, sql: String?, parameters: BoundParameters?) {
        if(parameters != null && !parameters.isEmpty) {
            logger.debug { "beforeExecuteQuery SQL:\n$sql ($parameters)" }
        } else {
            logger.debug { "beforeExecuteQuery SQL:\n$sql" }
        }

        if(printStatement)
            logger.debug { "$statement" }
    }
}