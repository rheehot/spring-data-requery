package com.coupang.springframework.data.requery.core;

import io.requery.TransactionIsolation;
import io.requery.sql.EntityDataStore;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;

/**
 * Java용 RequeryTemplate
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
public class RequeryTemplate implements RequeryOperations {

    private final EntityDataStore dataStore;

    public RequeryTemplate(EntityDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public EntityDataStore getDataStore() {
        return (EntityDataStore<Object>) dataStore;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V runInTransaction(Callable<V> callable, @Nullable TransactionIsolation isolation) {
        return (V) dataStore.runInTransaction(callable, isolation);
    }
}
