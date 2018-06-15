package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.annotation.Query;
import com.coupang.springframework.data.requery.core.RequeryOperations;
import io.requery.query.element.QueryElement;
import org.jetbrains.annotations.NotNull;

/**
 * {@link Query} annotation에 정의된 문자열에 대한 쿼리 문장
 *
 * @author debop@coupang.com
 * @since 18. 6. 15
 */
public class RawStringRequeryQuery extends AbstractRequeryQuery {

    public RawStringRequeryQuery(@NotNull RequeryQueryMethod method, @NotNull RequeryOperations operations) {
        super(method, operations);
    }

    @Override
    protected QueryElement<?> createQuery(RequeryParameterAccessor accessor, RequeryQueryOptions options) {
        return null;
    }

    @Override
    protected boolean isCountQuery() {

        // TODO: method의 Query annotation의 countQuery 정보를 기준으로 판단.
        return false;
    }

    @Override
    protected boolean isExistsQuery() {

        // TODO: 추후 추가해야 한다. 
        return false;
    }
}
