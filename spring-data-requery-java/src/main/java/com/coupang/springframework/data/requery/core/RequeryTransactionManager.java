package com.coupang.springframework.data.requery.core;

import io.requery.TransactionIsolation;
import io.requery.sql.EntityDataStore;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * RequeryTransactionManager
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
@Slf4j
public class RequeryTransactionManager extends DataSourceTransactionManager {
    private static final long serialVersionUID = 3291422158479490099L;

    private final EntityDataStore entityDataStore;

    public RequeryTransactionManager(EntityDataStore entityDataStore, DataSource dataSource) {
        super(dataSource);
        this.entityDataStore = entityDataStore;
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        super.doBegin(transaction, definition);

        log.debug("Begin transaction. definition={}", definition);

        TransactionIsolation isolation = getTransactionIsolation(definition.getIsolationLevel());
        if (isolation != null) {
            entityDataStore.transaction().begin(isolation);
        } else {
            entityDataStore.transaction().begin();
        }
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        entityDataStore.transaction().commit();
        super.doCommit(status);
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        entityDataStore.transaction().rollback();
        super.doRollback(status);
    }

    @Override
    protected Object doSuspend(Object transaction) {
        return super.doSuspend(transaction);
    }

    @Override
    protected void doResume(Object transaction, Object suspendedResources) {
        super.doResume(transaction, suspendedResources);
    }

    @Nullable
    private TransactionIsolation getTransactionIsolation(int isolationLevel) {
        switch (isolationLevel) {
            case Connection.TRANSACTION_NONE:
                return TransactionIsolation.NONE;
            case Connection.TRANSACTION_READ_UNCOMMITTED:
                return TransactionIsolation.READ_UNCOMMITTED;
            case Connection.TRANSACTION_READ_COMMITTED:
                return TransactionIsolation.READ_COMMITTED;
            case Connection.TRANSACTION_REPEATABLE_READ:
                return TransactionIsolation.REPEATABLE_READ;
            case Connection.TRANSACTION_SERIALIZABLE:
                return TransactionIsolation.SERIALIZABLE;

            default:
                return null;
        }
    }
}
