package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.utils.RequeryMetamodel;
import com.coupang.springframework.data.requery.utils.RequeryUtils;
import io.requery.query.Scalar;
import io.requery.query.Tuple;
import io.requery.query.element.QueryElement;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ReturnedType;
import org.springframework.util.Assert;

import java.util.Optional;


/**
 * Abstract base class to implement {@link RepositoryQuery}s.
 *
 * @author debop
 * @since 18. 6. 7
 */
@Getter
@Slf4j
public abstract class AbstractRequeryQuery implements RepositoryQuery {

    protected final RequeryQueryMethod queryMethod;
    protected final RequeryOperations operations;
    protected final RequeryMetamodel metamodel;
    protected final Class<?> domainClass;

    public AbstractRequeryQuery(@NotNull final RequeryQueryMethod method,
                                @NotNull final RequeryOperations operations) {
        Assert.notNull(method, "queryMethod must not be null");
        Assert.notNull(operations, "operations must not be null");

        this.queryMethod = method;
        this.operations = operations;
        this.metamodel = new RequeryMetamodel(RequeryUtils.getEntityModel(operations.getDataStore()));
        this.domainClass = method.getEntityInformation().getJavaType();
    }

    public Object execute(Object[] parameters) {
        return doExecute(getExecution(), parameters);
    }

    @Nullable
    private Object doExecute(@NotNull RequeryQueryExecution execution, Object[] values) {

        Object result = execution.execute(this, values);
        ParametersParameterAccessor accessor = new ParametersParameterAccessor(queryMethod.getParameters(), values);

        log.debug("doExecute ... result={}", result);
        return result;
    }

    protected RequeryQueryExecution getExecution() {
        if (queryMethod.isStreamQuery()) {
            return new RequeryQueryExecution.StreamExecution();
        } else if (queryMethod.isCollectionQuery()) {
            return new RequeryQueryExecution.CollectionExecution();
        } else if (queryMethod.isSliceQuery()) {
            return new RequeryQueryExecution.SlicedExecution(queryMethod.getParameters());
        } else if (queryMethod.isPageQuery()) {
            return new RequeryQueryExecution.PagedExecution(queryMethod.getParameters());

            // TODO: UPSERT/INSERT/UPDATE 관련은 구현해야된다
//        } else if (queryMethod.isModifyingQuery()) {
//            return new RequeryQueryExecution.ModifyingExecution();
        } else {
            return new RequeryQueryExecution.SingleEntityExecution();
        }
    }


    protected QueryElement<?> createQueryElement(Object[] values) {
        log.debug("Create QueryElement with domainClass={}, values={}", domainClass.getName(), values);
        return doCreateQuery(values);
    }

    protected QueryElement<? extends Scalar<Integer>> createCountQueryElement(Object[] values) {
        return doCreateCountQuery(values);
    }

    protected Optional<Class<?>> getTypeToRead(ReturnedType returnedType) {
        return returnedType.isProjecting() && !getMetamodel().isRequeryManaged(returnedType.getReturnedType())
               ? Optional.of(Tuple.class)
               : Optional.empty();
    }

    protected abstract QueryElement<?> doCreateQuery(Object[] values);

    protected abstract QueryElement<? extends Scalar<Integer>> doCreateCountQuery(Object[] values);


}

