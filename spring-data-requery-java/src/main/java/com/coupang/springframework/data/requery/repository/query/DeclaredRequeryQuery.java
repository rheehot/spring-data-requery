package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.annotation.Query;
import com.coupang.springframework.data.requery.core.RequeryOperations;
import io.requery.query.Result;
import io.requery.query.Scalar;
import io.requery.query.Tuple;
import io.requery.query.element.QueryElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.data.repository.query.ReturnedType;
import org.springframework.util.StringUtils;

/**
 * {@link Query} annotation이 정의된 메소드, interface default method, custom defined method를 실행하는 {@link RepositoryQuery}
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
        throw new UnsupportedOperationException("Unsupported operation in DeclaredRequeryQuery is defined");
    }

    @Override
    protected QueryElement<? extends Scalar<Integer>> doCreateCountQuery(Object[] values) {
        throw new UnsupportedOperationException("Unsupported operation in @Query is defined");
    }

    @Override
    public Object execute(Object[] parameters) {
        if (getQueryMethod().isAnnotatedQuery()) {
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
        } else if (getQueryMethod().isDefaultMethod()) {
            // TODO: interface default method 일 경우 처리
            throw new NotImplementedException("Interface default method 실행 구현 중");
        } else if (getQueryMethod().isOverridedMethod()) {
            // TODO: custom implemented method 일 경우 처리
            throw new NotImplementedException("Custom implmented method 실행 구현 중");
        }

        return null;
    }

    @Nullable
    private Object castResult(Result<?> result) {
        if (getQueryMethod().isCollectionQuery()) {
            return result.toList();
        } else if (getQueryMethod().isStreamQuery()) {
            return result.stream();
        } else {
            Object value = result.firstOrNull();

            // TODO: 중복 제거 필요 
            if (value instanceof Tuple) {
                Tuple tuple = (Tuple) value;
                if (tuple.count() == 1) {
                    return tuple.get(0);
                }
                return tuple;
            }
            return value;
        }
    }

    @NotNull
    private String getRawQuery() {

        String rawQuery = getQueryMethod().getAnnotatedQuery();
        log.trace("Get raw query = {}", rawQuery);

        if (StringUtils.isEmpty(rawQuery)) {
            throw new IllegalStateException("No @Query query specified on " + queryMethod.getName());
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
