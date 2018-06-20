package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.mapping.RequeryMappingContext;
import com.coupang.springframework.data.requery.provider.RequeryPersistenceProvider;
import com.coupang.springframework.data.requery.utils.RequeryUtils;
import io.requery.query.NamedExpression;
import io.requery.query.Result;
import io.requery.query.element.QueryElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.query.parser.PartTree;

/**
 * {@link PartTree} 정보를 바탕으로 Requery {@link QueryElement} 를 빌드합니다.
 *
 * @author debop
 * @since 18. 6. 9
 */
@Slf4j
public class PartTreeRequeryQuery extends AbstractRequeryQuery {

    private final PartTree tree;
    private final RequeryParameters parameters;

    // private final QueryPreparer query;
    // private final QueryPreparer countQuery;
    private final RequeryOperations operations;
    private final RequeryMappingContext context;


    public PartTreeRequeryQuery(@NotNull RequeryQueryMethod method,
                                @NotNull RequeryOperations operations,
                                @NotNull RequeryPersistenceProvider persistenceProvider) {
        super(method, operations);

        this.operations = operations;
        this.context = operations.getMappingContext();

        Class<?> domainClass = method.getEntityInformation().getJavaType();
        this.parameters = method.getParameters();

        boolean recreationRequired = parameters.hasDynamicProjection() || parameters.potentiallySortsDynamically();

        log.debug("Create PartTreeRequeryQuery. domainClass={}, parameters={}", domainClass, parameters);

        try {
            this.tree = new PartTree(method.getName(), domainClass);
            // this.countQuery = new CountQueryPreparer(persistenceProvider, recreationRequired);
            // this.query = tree.isCountProjection() ? countQuery : new QueryPreparer(persistenceProvider, recreationRequired);
        } catch (Exception e) {
            throw new IllegalArgumentException("Fail to create PartTreeRequeryQuery for method " + method, e);
        }
    }


    @Override
    protected QueryElement<?> doCreateQuery(Object[] values) {
        throw new NotImplementedException("구현 중");
    }

    @Override
    protected QueryElement<?> doCreateCountQuery(Object[] values) {
        throw new NotImplementedException("구현 중");
    }

//    @Override
//    protected QueryElement<?> createQuery(RequeryParameterAccessor accessor, RequeryQueryOptions options) {
//        RequeryPersistenceProvider persistenceProvider = null;
//        ParameterMetadataProvider provider = new ParameterMetadataProvider(accessor.getParameters(), persistenceProvider);
//
//        ResultProcessor processor = getQueryMethod().getResultProcessor();
//        ReturnedType returnedType = processor.withDynamicProjection(accessor).getReturnedType();
//
//        return (QueryElement<?>) new RequeryQueryCreator(operations,
//                                                         provider,
//                                                         returnedType,
//                                                         tree,
//                                                         accessor,
//                                                         null)
//            .createQuery();
//    }

    // 아래 부분이 RequeryQueryCreator 로 이동해야 한다.
    //

    @SuppressWarnings("unchecked")
    protected QueryElement<?> prepareQuery(RequeryParameterAccessor accessor) {
        QueryElement<? extends Result<?>> query = (QueryElement<? extends Result<?>>) getOperations().select(getDomainClass());

        query = buildWhereClause(query, accessor);

        if (accessor.getParameters().hasPageableParameter()) {
            query = RequeryUtils.applyPageable(getDomainClass(), query, accessor.getPageable());
        }
        if (accessor.getParameters().hasSortParameter()) {
            query = RequeryUtils.applySort(getDomainClass(), query, accessor.getSort());
        }

        return query;
    }

    @SuppressWarnings("unchecked")
    protected QueryElement<? extends Result<?>> buildWhereClause(QueryElement<? extends Result<?>> selection,
                                                                 RequeryParameterAccessor accessor) {

        final RequeryParameters bindableParams = accessor.getParameters().getBindableParameters();
        final int bindableParamsSize = bindableParams.getNumberOfParameters();

        QueryElement<? extends Result<?>>[] selections = new QueryElement[1];

        selections[0] = selection;
        for (int i = 0; i < bindableParamsSize; i++) {
            final RequeryParameters.RequeryParameter param = bindableParams.getParameter(i);
            final Object value = accessor.getBindableValue(i);

            if (param.isNamedParameter() && param.getName().isPresent()) {
                NamedExpression expr = NamedExpression.of(param.getName().get(), value.getClass());
                selections[0] = (QueryElement<? extends Result<?>>) selection.where(expr.eq(value));
            }
        }
        return selections[0];
    }


    /**
     * Query preparer to create {@link QueryElement} instances and potentially cache them.
     */
    private class QueryPreparer {

    }

    /**
     * Special {@link QueryPreparer} to create count queries.
     */
    private class CountQueryPreparer extends QueryPreparer {

    }
}
