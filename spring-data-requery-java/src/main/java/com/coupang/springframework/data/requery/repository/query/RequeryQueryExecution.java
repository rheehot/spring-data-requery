package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.NotSupportedException;
import com.coupang.springframework.data.requery.core.RequeryOperations;
import io.requery.query.Result;
import io.requery.query.Scalar;
import io.requery.query.Selection;
import io.requery.query.Tuple;
import io.requery.query.element.QueryElement;
import io.requery.query.function.Count;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Set of classes to contain query execution strategies. Depending (mostly) on the return type of a
 * {@link org.springframework.data.repository.query.QueryMethod} a {@link DeclaredRequeryQuery} can be executed
 * in various flavors.
 *
 * @author debop
 * @since 18. 6. 7
 */
@Slf4j
public abstract class RequeryQueryExecution {

    private static final ConversionService CONVERSION_SERVICE;

    static {

        ConfigurableConversionService conversionService = new DefaultConversionService();

        // Blob to Byte array 로 하는 것은 BlobByteArrayConverter 를 사용하면 된다.
        // conversionService.addConverter(JpaResultConverters.BlobToByteArrayConverter.INSTANCE);

        conversionService.removeConvertible(Collection.class, Object.class);
        potentiallyRemoveOptionalConverter(conversionService);

        CONVERSION_SERVICE = conversionService;
    }

    /**
     * Removes the converter being able to convert any object into an {@link Optional} from the given
     * {@link ConversionService} in case we're running on Java 8.
     *
     * @param conversionService must not be {@literal null}.
     */
    public static void potentiallyRemoveOptionalConverter(ConfigurableConversionService conversionService) {

        ClassLoader classLoader = RequeryQueryExecution.class.getClassLoader();

        if (ClassUtils.isPresent("java.util.Optional", classLoader)) {
            try {
                Class<?> optionalType = ClassUtils.forName("java.util.Optional", classLoader);
                conversionService.removeConvertible(Object.class, optionalType);
            } catch (ClassNotFoundException | LinkageError o_O) {
                // Nothing to do.
            }
        }
    }

    /**
     * Executes the given {@link DeclaredRequeryQuery} with the given {@link ParameterBinder}.
     */
    @Nullable
    public Object execute(@NotNull AbstractRequeryQuery query, @NotNull Object[] values) {

        Assert.notNull(query, "AbstractRequeryQuery must not be null!");
        Assert.notNull(values, "Values must not be null!");

        Object result;

        try {
            result = doExecute(query, values);
        } catch (io.requery.PersistenceException pe) {
            log.error("Fail to doExecute. query={}", query, pe);
            return null;
        } catch (Exception e) {
            log.error("Fail to execute. query={}", query, e);
            return null;
        }

        return result;
    }

    /**
     * Method to implement {@link DeclaredRequeryQuery} executions by single enum values.
     */
    @Nullable
    protected abstract Object doExecute(AbstractRequeryQuery query, Object[] values);


    //
    //
    //


    /**
     * Executes the query to return a simple collection of entities.
     */
    static class CollectionExecution extends RequeryQueryExecution {
        @Override
        protected @Nullable Object doExecute(AbstractRequeryQuery query, Object[] values) {
            Result<?> result = (Result<?>) query.createQueryElement(values).get();
            return result.toList();
        }
    }

    /**
     * Executes the query to return a {@link Slice} of entities.
     */
    static class SlicedExecution extends RequeryQueryExecution {

        private final RequeryParameters parameters;

        public SlicedExecution(RequeryParameters parameters) {
            this.parameters = parameters;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected @Nullable SliceImpl doExecute(AbstractRequeryQuery query, Object[] values) {
            ParametersParameterAccessor accessor = new ParametersParameterAccessor(parameters, values);
            Pageable pageable = accessor.getPageable();
            int pageSize = pageable.getPageSize();

            QueryElement<?> queryElement = query.createQueryElement(values);

            Result<Object> result = (Result<Object>) queryElement.limit(pageSize).get();
            List<Object> resultList = result.toList();
            boolean hasNext = resultList.size() > pageSize;

            return new SliceImpl(hasNext ? resultList.subList(0, pageSize) : resultList, pageable, hasNext);
        }
    }


    static class StreamExecution extends RequeryQueryExecution {

        @Override
        @SuppressWarnings("unchecked")
        protected @Nullable Stream<?> doExecute(AbstractRequeryQuery query, Object[] values) {
            Result<?> result = (Result<?>) query.createQueryElement(values).get();
            return result.stream();
        }
    }

    static class PagedExecution extends RequeryQueryExecution {

        private final RequeryParameters parameters;

        public PagedExecution(RequeryParameters parameters) {
            this.parameters = parameters;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected @Nullable Page<?> doExecute(AbstractRequeryQuery query, Object[] values) {
            ParameterAccessor accessor = new ParametersParameterAccessor(parameters, values);
            QueryElement<?> queryElement = query.createQueryElement(values);

            //
            // TODO: Sort 와 관련된 부분도 처리해야 한다.
            //
            Pageable pageable = accessor.getPageable();
            int limit = pageable.getPageSize();
            int offset = (int) pageable.getOffset();

            Result<?> result = (Result<?>) queryElement.limit(limit).offset(offset).get();

            return new PageImpl(result.toList(), pageable, count(query, values));
        }

        @SuppressWarnings("unchecked")
        private long count(AbstractRequeryQuery query, Object[] values) {
            QueryElement<?> queryElement = query.createQueryElement(values);
            Selection<?> selection = query.getOperations().select(Count.count(query.getDomainClass()));

            Scalar<Integer> result = (Scalar<Integer>) queryElement.select(Count.count(query.getDomainClass())).get();
            return result.value();
        }
    }


    static class SingleEntityExecution extends RequeryQueryExecution {

        @Override
        protected @Nullable Object doExecute(AbstractRequeryQuery query, Object[] values) {
            log.debug("Get single entity. query={}, values={}", query, values);
            Result<?> result = (Result<?>) query.createQueryElement(values).get();
            Object value = result.firstOrNull();

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


    static class ModifyingExecution extends RequeryQueryExecution {
        private final RequeryOperations operations;
        private final boolean isLong;
        private final boolean isInt;
        private final boolean isVoid;
        private final CommandType commandType;

        public ModifyingExecution(@NotNull RequeryQueryMethod method, @NotNull RequeryOperations operations) {

            Assert.notNull(method, "method must not be null!");
            Assert.notNull(operations, "operation must not be null!");

            Class<?> returnType = method.getReturnType();

            isVoid = void.class.equals(returnType) || Void.class.equals(returnType);
            isInt = int.class.equals(returnType) || Integer.class.equals(returnType);
            isLong = long.class.equals(returnType) || Long.class.equals(returnType);

            commandType = CommandType.parse(method.getName());

            this.operations = operations;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected @Nullable Object doExecute(AbstractRequeryQuery query, Object[] values) {
            throw new NotSupportedException("구현 중");
        }
    }

    static class DeleteExecution extends RequeryQueryExecution {

        private final RequeryOperations operations;

        public DeleteExecution(@NotNull RequeryOperations operations) {
            Assert.notNull(operations, "operations must not be null!");
            this.operations = operations;
        }

        @Override
        protected @Nullable Object doExecute(AbstractRequeryQuery query, Object[] values) {

            QueryElement<?> deleteQuery = query.createQueryElement(values);

            //
            // TODO: 삭제관련 효율을 높히기 위한 방안을 찾아야한다.
            //

            Result<?> result = (Result<?>) deleteQuery.get();
            return result.first();
        }
    }

    static class ExistsExecution extends RequeryQueryExecution {
        @Override
        protected @Nullable Object doExecute(AbstractRequeryQuery query, Object[] values) {
            Result<?> result = (Result<?>) query.createQueryElement(values).limit(1).get();

            return result.firstOrNull() != null;
        }
    }
}
