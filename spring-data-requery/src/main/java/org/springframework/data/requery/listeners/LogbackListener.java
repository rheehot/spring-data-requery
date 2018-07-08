package org.springframework.data.requery.listeners;

import io.requery.sql.BoundParameters;
import io.requery.sql.EntityStateListener;
import io.requery.sql.StatementListener;
import lombok.extern.slf4j.Slf4j;

import java.sql.Statement;

/**
 * org.springframework.data.requery.listeners.LogbackListener
 *
 * @author debop
 */
@Slf4j
public class LogbackListener<E> implements EntityStateListener<E>, StatementListener {

    private final boolean printStatement;

    public LogbackListener() {
        this(false);
    }

    public LogbackListener(boolean printStatement) {
        this.printStatement = printStatement;
    }

    @Override
    public void postDelete(E entity) {
        log.debug("postDelete {}", entity);
    }

    @Override
    public void postInsert(E entity) {
        log.debug("postInsert {}", entity);
    }

    @Override
    public void postLoad(E entity) {
        log.debug("postLoad {}", entity);
    }

    @Override
    public void postUpdate(E entity) {
        log.debug("postUpdate {}", entity);
    }

    @Override
    public void preDelete(E entity) {
        log.debug("preDelete {}", entity);
    }

    @Override
    public void preInsert(E entity) {
        log.debug("preInsert {}", entity);
    }

    @Override
    public void preUpdate(E entity) {
        log.debug("preUpdate {}", entity);
    }

    @Override
    public void beforeExecuteUpdate(Statement statement, String sql, BoundParameters parameters) {

    }

    @Override
    public void afterExecuteUpdate(Statement statement, int count) {
        log.debug("afterExecuteUpdate count={}", count);
    }

    @Override
    public void beforeExecuteBatchUpdate(Statement statement, String sql) {
        if (statement != null) {
            log.debug("beforeExecuteBatchUpdate SQL:\n{}", sql);

            if (printStatement) {
                log.debug("{}", statement);
            }
        }
    }

    @Override
    public void afterExecuteBatchUpdate(Statement statement, int[] count) {
        log.debug("afterExecuteBatchUpdate count={}", count);
    }

    @Override
    public void beforeExecuteQuery(Statement statement, String sql, BoundParameters parameters) {
        if (statement != null) {
            if (parameters != null && !parameters.isEmpty()) {
                log.debug("beforeExecuteUpdate SQL:\n{} ({})", sql, parameters);
            } else {
                log.debug("beforeExecuteUpdate SQL:\n{}", sql);
            }

            if (printStatement) {
                log.debug("{}", statement);
            }
        }
    }

    @Override
    public void afterExecuteQuery(Statement statement) {
        log.debug("afterExecuteQuery");
    }
}
