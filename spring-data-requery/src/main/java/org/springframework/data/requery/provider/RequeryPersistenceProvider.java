package org.springframework.data.requery.provider;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.requery.core.RequeryOperations;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * org.springframework.data.requery.provider.RequeryPersistenceProvider
 *
 * @author debop
 * @since 18. 6. 9
 */
@Deprecated
@Slf4j
public class RequeryPersistenceProvider implements ProxyIdAccessor {

    public static RequeryPersistenceProvider of(@NotNull RequeryOperations operations) {
        Assert.notNull(operations, "operation must not be null!");
        return new RequeryPersistenceProvider(operations);
    }

    private final RequeryOperations operations;

    public RequeryPersistenceProvider(@NotNull RequeryOperations operations) {
        Assert.notNull(operations, "operations must not be null!");
        this.operations = operations;
    }

    @Override
    public boolean shouldUseAccessorFor(Object entity) {
        return false;
    }

    @Nullable
    @Override
    public Object getIdentifierFrom(Object entity) {
        return null;
    }

    public <T> Collection<T> potentiallyConvertEmptyCollection(@Nullable Collection<T> collection) {
        return collection == null || collection.isEmpty() ? null : collection;
    }
}
