package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.annotation.Query;
import com.coupang.springframework.data.requery.core.RequeryOperations;
import io.requery.query.Result;
import io.requery.query.Scalar;
import io.requery.query.Tuple;
import io.requery.query.element.QueryElement;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.data.repository.query.ReturnedType;
import org.springframework.util.StringUtils;

import java.util.List;

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

    @SuppressWarnings("unchecked")
    @Override
    public Object execute(Object[] parameters) {

        String query = getRawQuery();

        log.debug("Execute queryMethod={}, return type={}, query={}", getQueryMethod().getName(), getQueryMethod().getReturnType(), query);

        Result<?> result;

        // TODO: Refactoring이 필요하다.
        // TODO: Entity 나 Tuple 에 대해 ReturnedType에 맞게 casting 해야 한다.
        // TODO: Paging 에 대해서는 처리했는데, Sort 는 넣지 못했음. 이 부분도 추가해야 함.

        if (getQueryMethod().isPageQuery()) {
            RequeryParametersParameterAccessor accessor = new RequeryParametersParameterAccessor(getQueryMethod().getParameters(), parameters);
            Pageable pageable = accessor.getPageable();

            int pageableIndex = accessor.getParameters().getPageableIndex();

            Object[] values = (pageableIndex >= 0) ? new Object[parameters.length - 1] : parameters;

            for (int i = 0, j = 0; i < parameters.length; i++) {
                if (i == pageableIndex) {
                    continue;
                }
                values[j++] = parameters[i];
            }

            if (pageable.isUnpaged()) {
                List<?> rows = operations.raw(getQueryMethod().getEntityInformation().getJavaType(), query, values).toList();
                return new PageImpl(rows);
            } else {

                String countQuery = "select count(cnt_tbl.*) from (" + query + ") as cnt_tbl";
                Tuple countResult = operations.raw(countQuery, values).first();

                long offset = pageable.getOffset();
                int limit = pageable.getPageSize();

                query = query + " offset " + offset + " limit " + limit;
                List<?> rows = (getQueryMethod().isQueryForEntity())
                               ? operations.raw(getQueryMethod().getEntityInformation().getJavaType(), query, values).toList()
                               : operations.raw(query, values).toList();

                return new PageImpl(rows, pageable, countResult.<Long>get(0));
            }
        }

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
        // TODO: List<Tuple> 인 경우 returned type 으로 변경해야 한다.

        if (getQueryMethod().isCollectionQuery()) {
            return result.toList();
        } else if (getQueryMethod().isStreamQuery()) {
            return result.stream();
        } else {
            return RequeryResultConverter.convertResult(result.firstOrNull());
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
