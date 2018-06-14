package com.coupang.springframework.data.requery.core;

import io.requery.TransactionIsolation;
import io.requery.sql.EntityDataStore;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * Java용 RequeryTemplate
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@Slf4j
public class RequeryTemplate implements RequeryOperations {

    private final EntityDataStore<Object> dataStore;

    public RequeryTemplate(EntityDataStore<Object> dataStore) {
        this.dataStore = dataStore;
    }

    public EntityDataStore<Object> getDataStore() {
        return dataStore;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V runInTransaction(Callable<V> callable, @Nullable TransactionIsolation isolation) {
        Assert.notNull(callable, "Callable must not be null.");
        return dataStore.runInTransaction(callable, isolation);
    }

    @Override
    public <V> V withTransaction(Function<EntityDataStore<Object>, V> block,
                                 @Nullable TransactionIsolation isolation) {
        return getDataStore().runInTransaction(() -> block.apply(dataStore), isolation);
    }
}
