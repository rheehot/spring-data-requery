package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.utils.RequeryMetamodel;
import com.coupang.springframework.data.requery.utils.RequeryUtils;
import io.requery.query.Result;
import io.requery.query.Tuple;
import io.requery.query.element.QueryElement;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ReturnedType;

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

    protected final RequeryQueryMethod method;
    protected final RequeryOperations operations;
    protected final RequeryMetamodel metamodel;
    protected final Class<?> domainClass;


    public AbstractRequeryQuery(@NotNull final RequeryQueryMethod method,
                                @NotNull final RequeryOperations operations) {
        this.method = method;
        this.operations = operations;
        this.metamodel = new RequeryMetamodel(RequeryUtils.getEntityModel(operations.getDataStore()));
        this.domainClass = method.getEntityInformation().getJavaType();
    }

    @Override
    public RequeryQueryMethod getQueryMethod() {
        return method;
    }

    public Object execute(Object[] parameters) {

        return doExecute(getExecution(), parameters);

//        final RequeryParameterAccessor accessor = new RequeryParametersParameterAccessor(method, parameters);
//        final RequeryQueryOptions options = method.getAnnotatedQueryOptions();
//
//        if (method.isPageQuery()) {
//            options.setCount(true);
//        }
//
//        final ResultProcessor processor = method.getResultProcessor().withDynamicProjection(accessor);
//        final Class<?> typeToRead = getTypeToRead(processor);
//
//        QueryElement<?> query = createQueryElement(accessor, options);
//
//        return typeToRead.cast(query.get());
    }

    private Object doExecute(RequeryQueryExecution execution, Object[] values) {

        Object result = execution.execute(this, values);

//        ParametersParameterAccessor accessor = new ParametersParameterAccessor(method.getParameters(), values);
//        ResultProcessor withDynamicProjection = method.getResultProcessor().withDynamicProjection(accessor);

        log.debug("doExecute ... result={}", result);
        return result;
    }

    protected RequeryQueryExecution getExecution() {
        if (method.isStreamQuery()) {
            return new RequeryQueryExecution.StreamExecution();
        } else if (method.isCollectionQuery()) {
            return new RequeryQueryExecution.CollectionExecution();
        } else if (method.isSliceQuery()) {
            return new RequeryQueryExecution.SlicedExecution(method.getParameters());
        } else if (method.isPageQuery()) {
            return new RequeryQueryExecution.PagedExecution(method.getParameters());

            // TODO: UPSERT/INSERT/UPDATE 관련은 구현해야된다
//        } else if (method.isModifyingQuery()) {
//            return new RequeryQueryExecution.ModifyingExecution();
        } else {
            return new RequeryQueryExecution.SingleEntityExecution();
        }
    }


    protected QueryElement<? extends Result<?>> createQueryElement(Object[] values) {
        return doCreateQuery(values);
    }

    protected QueryElement<? extends Result<?>> createCountQueryElement(Object[] values) {
        return doCreateCountQuery(values);
    }

    protected Optional<Class<?>> getTypeToRead(ReturnedType returnedType) {
        return returnedType.isProjecting() && !getMetamodel().isRequeryManaged(returnedType.getReturnedType())
               ? Optional.of(Tuple.class)
               : Optional.empty();
    }

    protected abstract QueryElement<? extends Result<?>> doCreateQuery(Object[] values);

    protected abstract QueryElement<? extends Result<?>> doCreateCountQuery(Object[] values);


}

