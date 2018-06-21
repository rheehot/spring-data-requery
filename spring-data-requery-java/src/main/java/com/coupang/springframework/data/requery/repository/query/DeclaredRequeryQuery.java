package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.annotation.Query;
import com.coupang.springframework.data.requery.core.RequeryOperations;
import io.requery.query.Result;
import io.requery.query.Scalar;
import io.requery.query.element.QueryElement;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.data.repository.query.ReturnedType;
import org.springframework.util.StringUtils;

/**
 * {@link Query} annotation에 정의된 문자열에 대한 쿼리 문장
 *
 * @author debop@coupang.com
 * @since 18. 6. 15
 */
@Slf4j
public class DeclaredRequeryQuery extends AbstractRequeryQuery {

    public DeclaredRequeryQuery(@NotNull RequeryQueryMethod method,
                                @NotNull RequeryOperations operations) {
        super(method, operations);
    }

    @Override
    protected QueryElement<? extends Result<?>> doCreateQuery(Object[] values) {
        throw new UnsupportedOperationException("Unsupported operation in @Query is defined");
    }

    @Override
    protected QueryElement<? extends Scalar<Integer>> doCreateCountQuery(Object[] values) {
        throw new UnsupportedOperationException("Unsupported operation in @Query is defined");
    }

    @Override
    public Object execute(Object[] parameters) {
        String query = getRawQuery();

        log.debug("Execute queryMethod={}, query={}, return type={}", getQueryMethod().getName(), getQueryMethod().getReturnType(), query);

        Result<?> result;
        if (getQueryMethod().isQueryForEntity()) {
            log.debug("Query for entity. entity={}", getQueryMethod().getEntityInformation().getJavaType());
            result = operations.raw(getQueryMethod().getEntityInformation().getJavaType(), query, parameters);
            return castResult(result);
        } else {
            result = operations.raw(query, parameters);
            return castResult(result);
        }
    }

    @Nullable
    private Object castResult(Result<?> result) {
        if (getQueryMethod().isCollectionQuery()) {
            return result.toList();
        } else if (getQueryMethod().isStreamQuery()) {
            return result.stream();
        } else {
            return result.firstOrNull();
        }
    }

    @NotNull
    private String getRawQuery() {

        String rawQuery = getQueryMethod().getAnnotatedQuery();
        log.trace("Get raw query = {}", rawQuery);

        if (StringUtils.isEmpty(rawQuery)) {
            throw new IllegalStateException("No query specified on " + queryMethod.getName());
        }

        return rawQuery;
    }

    @NotNull
    private ReturnedType getReturnedType(Object[] parameters) {
        RequeryParametersParameterAccessor accessor = new RequeryParametersParameterAccessor(queryMethod, parameters);
        ResultProcessor processor = getQueryMethod().getResultProcessor();

        return processor.withDynamicProjection(accessor).getReturnedType();
    }


}
