package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import io.requery.query.NamedExpression;
import io.requery.query.Result;
import io.requery.query.Selection;
import io.requery.query.WhereAndOr;

/**
 * DefaultRequeryQuery
 *
 * @author debop@coupang.com
 * @since 18. 6. 8
 */
public class DefaultRequeryQuery extends AbstractRequeryQuery {

    private final String rawQuery;

    public DefaultRequeryQuery(RequeryQueryMethod method, RequeryOperations operations) {
        this(method.getAnnotatedQuery(), method, operations);
    }

    public DefaultRequeryQuery(String rawQuery, RequeryQueryMethod method, RequeryOperations operations) {
        super(method, operations);

        this.rawQuery = rawQuery;
    }

    @Override
    protected Selection<Result<?>> createQuery(RequeryParameterAccessor accessor, RequeryQueryOptions options) {
        return prepareQuery(accessor);
    }

    @Override
    protected boolean isCountQuery() {
        return false;
    }

    @Override
    protected boolean isExistsQuery() {
        return false;
    }


    protected Selection<Result<?>> prepareQuery(RequeryParameterAccessor accessor) {
        Selection<Result<?>> query = (Selection<Result<?>>) getOperations().getDataStore().select(getDomainClass());

        query = buildWhereClause(query, accessor);

        if (accessor.getParameters().hasPageableParameter()) {
            query = QueryElementUtils.addPaging(getDomainClass(), query, accessor.getPageable());
        }
        if (accessor.getParameters().hasSortParameter()) {
            query = QueryElementUtils.addSort(getDomainClass(), query, accessor.getSort());
        }

        return query;
    }

    protected Selection<Result<?>> buildWhereClause(Selection<Result<?>> selection, RequeryParameterAccessor accessor) {

        final RequeryParameters bindableParams = accessor.getParameters().getBindableParameters();
        final int bindableParamsSize = bindableParams.getNumberOfParameters();

        WhereAndOr<Result<?>> whereAndOr = null;

        for (int i = 0; i < bindableParamsSize; i++) {
            final RequeryParameters.RequeryParameter param = bindableParams.getParameter(i);
            final Object value = accessor.getBindableValue(i);

            if (param.isNamedParameter()) {
                NamedExpression expr = NamedExpression.of(param.getName().get(), value.getClass());

                if (whereAndOr == null) {
                    whereAndOr = selection.where(expr.eq(value));
                } else {
                    whereAndOr = whereAndOr.or(expr.eq(value));
                }
            }
        }

        return (Selection<Result<?>>) whereAndOr;
    }

}
