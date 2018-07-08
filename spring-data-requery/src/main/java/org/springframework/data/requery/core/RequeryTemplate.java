package org.springframework.data.requery.core;

import io.requery.TransactionIsolation;
import io.requery.sql.EntityDataStore;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationContext;
import org.springframework.data.requery.mapping.RequeryMappingContext;
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
@Getter
public class RequeryTemplate implements RequeryOperations {

    private final ApplicationContext applicationContext;
    private final EntityDataStore<Object> dataStore;
    private final RequeryMappingContext mappingContext;

    public RequeryTemplate(@NotNull ApplicationContext applicationContext,
                           @NotNull EntityDataStore<Object> dataStore,
                           @NotNull RequeryMappingContext mappingContext) {

        Assert.notNull(applicationContext, "applicationContext must not be null");
        Assert.notNull(dataStore, "dataStore must not be null");
        Assert.notNull(mappingContext, "mappingContext must not be null");

        this.applicationContext = applicationContext;
        this.dataStore = dataStore;
        this.mappingContext = mappingContext;
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
