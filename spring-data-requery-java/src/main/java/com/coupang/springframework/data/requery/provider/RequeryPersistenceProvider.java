package com.coupang.springframework.data.requery.provider;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Query;

/**
 * com.coupang.springframework.data.requery.provider.RequeryPersistenceProvider
 *
 * @author debop
 * @since 18. 6. 9
 */
public class RequeryPersistenceProvider implements QueryExtractor, ProxyIdAccessor {

    private final RequeryOperations operations;

    public RequeryPersistenceProvider(RequeryOperations operations) {
        this.operations = operations;
    }

    @Override
    public boolean shouldUseAccessorFor(Object entity) {
        throw new NotImplementedException("구현 중");
    }

    @Override
    public @Nullable Object getIdentifierFrom(Object entity) {
        throw new NotImplementedException("구현 중");
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
}
