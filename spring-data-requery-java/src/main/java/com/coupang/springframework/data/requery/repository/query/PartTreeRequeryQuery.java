package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.mapping.RequeryMappingContext;
import com.coupang.springframework.data.requery.provider.RequeryPersistenceProvider;
import com.coupang.springframework.data.requery.repository.query.RequeryQueryExecution.DeleteExecution;
import com.coupang.springframework.data.requery.repository.query.RequeryQueryExecution.ExistsExecution;
import io.requery.query.NamedExpression;
import io.requery.query.Scalar;
import io.requery.query.element.QueryElement;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.data.repository.query.ReturnedType;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.util.Assert;

import static com.coupang.springframework.data.requery.utils.RequeryUtils.*;

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

    private final QueryPreparer queryPreparer;
    private final CountQueryPreparer countQueryPreparer;
    private final RequeryOperations operations;
    private final RequeryMappingContext context;


    public PartTreeRequeryQuery(@NotNull RequeryQueryMethod method,
                                @NotNull RequeryOperations operations,
                                @NotNull RequeryPersistenceProvider persistenceProvider) {
        super(method, operations);

        Assert.notNull(persistenceProvider, "PersistenceProvider must not be null");

        this.operations = operations;
        this.context = operations.getMappingContext();

        Class<?> domainClass = method.getEntityInformation().getJavaType();
        this.parameters = method.getParameters();

        log.debug("Create PartTreeRequeryQuery. domainClass={}, parameters={}", domainClass, parameters);

        try {
            this.tree = new PartTree(method.getName(), domainClass);
            this.countQueryPreparer = new CountQueryPreparer(persistenceProvider);
            this.queryPreparer = tree.isCountProjection() ? countQueryPreparer : new QueryPreparer(persistenceProvider);
        } catch (Exception e) {
            throw new IllegalArgumentException("Fail to create query for method [" + method + "] message=" + e.getMessage(), e);
        }
    }


    @Override
    protected QueryElement<?> doCreateQuery(Object[] values) {
        return queryPreparer.createQuery(values);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected QueryElement<? extends Scalar<Integer>> doCreateCountQuery(Object[] values) {
        return (QueryElement<? extends Scalar<Integer>>) countQueryPreparer.createQuery(values);
    }

    @Override
    protected RequeryQueryExecution getExecution() {
        if (tree.isDelete()) {
            log.debug("Create DeleteExecution. queryMethod={}", queryMethod);
            return new DeleteExecution(operations);
        } else if (tree.isExistsProjection()) {
            log.debug("Create ExistsExecution. queryMethod={}", queryMethod);
            return new ExistsExecution();
        }
        return super.getExecution();
    }

    @SuppressWarnings("unchecked")
    protected QueryElement<?> prepareQuery(RequeryParameterAccessor accessor) {
        QueryElement<?> query = unwrap(getOperations().select(getDomainClass()));

        query = buildWhereClause(query, accessor);

        if (accessor.getParameters().hasPageableParameter()) {
            query = applyPageable(getDomainClass(), query, accessor.getPageable());
        }
        if (accessor.getParameters().hasSortParameter()) {
            query = applySort(getDomainClass(), query, accessor.getSort());
        }

        return query;
    }

    @SuppressWarnings("unchecked")
    protected QueryElement<?> buildWhereClause(QueryElement<?> baseQuery,
                                               RequeryParameterAccessor accessor) {

        final RequeryParameters bindableParams = accessor.getParameters().getBindableParameters();
        final int bindableParamsSize = bindableParams.getNumberOfParameters();

        QueryElement<?>[] queries = new QueryElement[1];

        queries[0] = baseQuery;
        for (int i = 0; i < bindableParamsSize; i++) {
            final RequeryParameters.RequeryParameter param = bindableParams.getParameter(i);
            final Object value = accessor.getBindableValue(i);

            if (param.isNamedParameter() && param.getName().isPresent()) {
                NamedExpression expr = NamedExpression.of(param.getName().get(), value.getClass());
                queries[0] = unwrap(baseQuery.where(expr.eq(value)));
            }
        }
        return queries[0];
    }


    /**
     * Query preparer to create {@link QueryElement} instances and potentially cache them.
     */
    private class QueryPreparer {

        private final RequeryPersistenceProvider persistenceProvider;

        QueryPreparer(@NotNull RequeryPersistenceProvider persistenceProvider) {
            Assert.notNull(persistenceProvider, "persistenceProvider must not be null!");
            this.persistenceProvider = persistenceProvider;

            // HINT: check wrong method (parameter number matching, not exists property name ...)
            RequeryQueryCreator creator = createCreator(persistenceProvider, null);
            if (creator != null) {
                creator.createQuery();
                creator.getParameterExpressions();
            }
        }

        public QueryElement<?> createQuery(Object[] values) {

            RequeryParametersParameterAccessor accessor = new RequeryParametersParameterAccessor(parameters, values);
            RequeryQueryCreator creator = createCreator(persistenceProvider, accessor);

            QueryElement<?> query = creator.createQuery(getDynamicSort(values));

            return restrictMaxResultsIfNecessary(query);
        }

        private QueryElement<?> restrictMaxResultsIfNecessary(QueryElement<?> baseQuery) {

            QueryElement<?> query = baseQuery;
            if (tree.isLimiting()) {
                if (query.getLimit() != null && query.getLimit() > 0) {
                    /*
                     * In order to return the correct results, we have to adjust the first result offset to be returned if:
                     * - a Pageable parameter is present
                     * - AND the requested page number > 0
                     * - AND the requested page size was bigger than the derived result limitation via the First/Top keyword.
                     */
                    if (query.getLimit() > tree.getMaxResults() && query.getOffset() > 0) {
                        query = unwrap(query.offset(query.getOffset() - (query.getLimit() - tree.getMaxResults())));
                    }
                }
                query = unwrap(query.limit(tree.getMaxResults()));
            }

            if (tree.isExistsProjection()) {
                query = unwrap(query.limit(1));
            }

            return query;
        }

        protected RequeryQueryCreator createCreator(final RequeryPersistenceProvider persistenceProvider,
                                                    final RequeryParametersParameterAccessor accessor) {

            ParameterMetadataProvider provider = (accessor != null)
                                                 ? new ParameterMetadataProvider(accessor, persistenceProvider)
                                                 : new ParameterMetadataProvider(parameters, persistenceProvider);

            ResultProcessor processor = getQueryMethod().getResultProcessor();
            ReturnedType returnedType = (accessor != null)
                                        ? processor.withDynamicProjection(accessor).getReturnedType()
                                        : processor.getReturnedType();

            return new RequeryQueryCreator(operations, provider, returnedType, tree);
        }

        private Sort getDynamicSort(Object[] values) {
            return parameters.potentiallySortsDynamically()
                   ? new RequeryParametersParameterAccessor(parameters, values).getSort()
                   : Sort.unsorted();
        }

    }

    /**
     * Special {@link QueryPreparer} to create count queries.
     */
    private class CountQueryPreparer extends QueryPreparer {

        CountQueryPreparer(@NotNull RequeryPersistenceProvider persistenceProvider) {
            super(persistenceProvider);
        }

        @Override
        protected RequeryCountQueryCreator createCreator(RequeryPersistenceProvider persistenceProvider,
                                                         RequeryParametersParameterAccessor accessor) {
            ParameterMetadataProvider provider = (accessor != null)
                                                 ? new ParameterMetadataProvider(accessor, persistenceProvider)
                                                 : new ParameterMetadataProvider(parameters, persistenceProvider);

            return new RequeryCountQueryCreator(operations,
                                                provider,
                                                getQueryMethod().getResultProcessor().getReturnedType(),
                                                tree);
        }
    }
}
