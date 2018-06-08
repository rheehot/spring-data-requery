package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.utils.EntityDataStoreUtils;
import com.coupang.springframework.data.requery.utils.RequeryMetamodel;
import io.requery.query.Result;
import io.requery.query.Selection;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ResultProcessor;


/**
 * com.coupang.springframework.data.requery.repository.query.AbstractRequeryQuery
 *
 * @author debop
 * @since 18. 6. 7
 */
@Getter
@Slf4j
public abstract class AbstractRequeryQuery implements RepositoryQuery {

    private final RequeryQueryMethod method;
    private final RequeryOperations operations;
    private final RequeryMetamodel metamodel;
    private final Class<?> domainClass;


    public AbstractRequeryQuery(@NotNull final RequeryQueryMethod method,
                                @NotNull final RequeryOperations operations) {
        this.method = method;
        this.operations = operations;
        this.metamodel = new RequeryMetamodel(EntityDataStoreUtils.getEntityModel(operations.getDataStore()));
        this.domainClass = method.getEntityInformation().getJavaType();
    }

    public Object execute(Object[] parameters) {
        final RequeryParameterAccessor accessor = new RequeryParametersParameterAccessor(method, parameters);
        final RequeryQueryOptions options = method.getAnnotatedQueryOptions();

        if (method.isPageQuery()) {
            options.setCount(true);
        }

        final ResultProcessor processor = method.getResultProcessor().withDynamicProjection(accessor);
        final Class<?> typeToRead = getTypeToRead(processor);

        Selection<Result<?>> query = createQuery(accessor, options);

        return typeToRead.cast(query.get());
    }

    @Override
    public RequeryQueryMethod getQueryMethod() {
        return method;
    }

    protected abstract Selection<Result<?>> createQuery(RequeryParameterAccessor accessor,
                                                        RequeryQueryOptions options);

    protected abstract boolean isCountQuery();

    protected abstract boolean isExistsQuery();


    protected RequeryQueryOptions mergeQueryOptions(RequeryQueryOptions oldStatic, RequeryQueryOptions newDynamic) {
        if (oldStatic == null) {
            return newDynamic;
        }
        if (newDynamic == null) {
            return oldStatic;
        }

        if (newDynamic.getCache() != null) {
            oldStatic.setCache(newDynamic.getCache());
        }
        if (newDynamic.getCount() != null) {
            oldStatic.setCount(newDynamic.getCount());
        }
        if (newDynamic.getFullCount() != null) {
            oldStatic.setFullCount(newDynamic.getFullCount());
        }

        return oldStatic;
    }

    private Class<?> getTypeToRead(final ResultProcessor processor) {
        if (isExistsQuery()) {
            return Integer.class;
        }
        final Class<?> typeToRead = processor.getReturnedType().getTypeToRead();
        return typeToRead != null ? typeToRead : Result.class;
    }

}
