package com.coupang.springframework.data.requery.provider;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.Assert;

import javax.persistence.Query;
import java.util.Collection;

/**
 * com.coupang.springframework.data.requery.provider.RequeryPersistenceProvider
 *
 * @author debop
 * @since 18. 6. 9
 */
@Slf4j
public class RequeryPersistenceProvider implements QueryExtractor, ProxyIdAccessor {

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

    @Override
    public String extractQueryString(Query query) {
        // TODO: Requery의 QueryBuilder 를 이용하여 구현해야 한다.
        throw new NotImplementedException("구현 중");
    }

    @Override
    public boolean canExtractQuery() {
        throw new NotImplementedException("구현 중");
    }

    public <T> Collection<T> potentiallyConvertEmptyCollection(@Nullable Collection<T> collection) {
        return collection == null || collection.isEmpty() ? null : collection;
    }
}
