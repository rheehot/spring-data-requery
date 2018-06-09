package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.provider.RequeryPersistenceProvider;
import io.requery.query.Result;
import io.requery.query.Selection;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.query.parser.PartTree;

/**
 * com.coupang.springframework.data.requery.repository.query.PartTreeRequeryQuery
 *
 * @author debop
 * @since 18. 6. 9
 */
public class PartTreeRequeryQuery extends AbstractRequeryQuery {

    private final PartTree tree;
    private final RequeryParameters parameters;

    // private final QueryPreparer query;
    // private final QueryPreparer countQuery;
    private final RequeryOperations operations;


    public PartTreeRequeryQuery(@NotNull RequeryQueryMethod method,
                                @NotNull RequeryOperations operations,
                                @NotNull RequeryPersistenceProvider persistenceProvider) {
        super(method, operations);

        this.operations = operations;
        Class<?> domainClass = method.getEntityInformation().getJavaType();
        this.parameters = method.getParameters();

        boolean recreationRequired = parameters.hasDynamicProjection() || parameters.potentiallySortsDynamically();

        try {
            this.tree = new PartTree(method.getName(), domainClass);
            // this.countQuery = new CountQueryPreparer(persistenceProvider, recreationRequired);
            // this.query = tree.isCountProjection() ? countQuery : new QueryPreparer(persistenceProvider, recreationRequired);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                String.format("Fail to create query for method %s! %s", method, e.getMessage()), e);
        }
    }

    @Override
    protected Selection<Result<?>> createQuery(RequeryParameterAccessor accessor, RequeryQueryOptions options) {
        throw new NotImplementedException("구현 중");
    }

    @Override
    protected boolean isCountQuery() {
        throw new NotImplementedException("구현 중");
    }

    @Override
    protected boolean isExistsQuery() {
        throw new NotImplementedException("구현 중");
    }
}
