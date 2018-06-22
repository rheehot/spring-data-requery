package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.provider.RequeryPersistenceProvider;
import io.requery.query.Expression;
import io.requery.query.FieldExpression;
import io.requery.query.NamedExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.Parameters;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.ParameterExpression;
import java.util.*;
import java.util.function.Supplier;

/**
 * Helper class to allow easy creation of {@link ParameterMetadata}s.
 *
 * @author debop
 * @since 18. 6. 14
 */
@Slf4j
public class ParameterMetadataProvider {

    private final Iterator<? extends Parameter> parameters;
    private final List<ParameterMetadata<?>> expressions;
    private final @Nullable Iterator<Object> bindableParameterValues;
    private final RequeryPersistenceProvider persistenceProvider;

    public ParameterMetadataProvider(ParametersParameterAccessor accessor,
                                     RequeryPersistenceProvider provider) {
        this(accessor.iterator(), accessor.getParameters(), provider);
    }

    public ParameterMetadataProvider(Parameters<?, ?> parameters,
                                     RequeryPersistenceProvider provider) {
        this(null, parameters, provider);
    }

    public ParameterMetadataProvider(@Nullable Iterator<Object> bindableParameterValues,
                                     Parameters<?, ?> parameters,
                                     RequeryPersistenceProvider provider) {

        Assert.notNull(parameters, "Parameters must not be null!");
        Assert.notNull(provider, "Provider must not be null!");

        this.parameters = parameters.getBindableParameters().iterator();
        this.expressions = new ArrayList<>();
        this.bindableParameterValues = bindableParameterValues;
        this.persistenceProvider = provider;
    }

    public List<ParameterMetadata<?>> getExpressions() { return Collections.unmodifiableList(expressions); }

    public <T> ParameterMetadata<T> next(Part part) {

        Assert.isTrue(parameters.hasNext(), "No parameter available for part. part=" + part);

        Parameter parameter = parameters.next();
        return (ParameterMetadata<T>) next(part, parameter.getType(), parameter);
    }

    public <T> ParameterMetadata<T> next(Part part, Class<T> type) {
        Parameter parameter = parameters.next();
        Class<?> typeToUse = ClassUtils.isAssignable(type, parameter.getType()) ? parameter.getType() : type;
        return (ParameterMetadata<T>) next(part, typeToUse, parameter);
    }

    private <T> ParameterMetadata<T> next(Part part, Class<T> type, Parameter parameter) {
        log.debug("get next parameter ... part={}, type={}, parameter={}", part, type, parameter);

        Assert.notNull(type, "Type must not be null!");

        /*
         * We treat Expression types as Object vales since the real value to be bound as a parameter is determined at query time.
         */
        Class<T> reifiedType = Expression.class.equals(type) ? (Class<T>) Object.class : type;

        Supplier<String> name = () -> parameter.getName()
            .orElseThrow(() -> new IllegalArgumentException("Parameter needs to be named"));

        NamedExpression<T> expression = parameter.isExplicitlyNamed()
                                        ? NamedExpression.of(name.get(), reifiedType)
                                        : NamedExpression.of(String.valueOf(parameter.getIndex()), reifiedType);

        Object value = bindableParameterValues == null ? ParameterMetadata.PLACEHOLDER : bindableParameterValues.next();

        ParameterMetadata metadata = new ParameterMetadata(expression, part.getType(), value, persistenceProvider);
        expressions.add(metadata);

        return metadata;
    }


    @Slf4j
    static class ParameterMetadata<T> {

        static final Object PLACEHOLDER = new Object();

        private final FieldExpression<T> expression;
        private final RequeryPersistenceProvider persistenceProvider;
        private final Part.Type type;
        private final Object value;

        /**
         * Creates a new {@link ParameterMetadata}.
         */
        public ParameterMetadata(FieldExpression<T> expression,
                                 Part.Type type,
                                 @Nullable Object value,
                                 RequeryPersistenceProvider provider) {

            this.expression = expression;
            this.persistenceProvider = provider;
            this.type = value == null && Part.Type.SIMPLE_PROPERTY.equals(type) ? Part.Type.IS_NULL : type;
            this.value = value;
        }

        /**
         * Returns the {@link ParameterExpression}.
         *
         * @return the expression
         */
        public FieldExpression<T> getExpression() {
            return expression;
        }

        public Object getValue() {
            return value;
        }

        /**
         * Returns whether the parameter shall be considered an {@literal IS NULL} parameter.
         */
        public boolean isIsNullParameter() {
            return Part.Type.IS_NULL.equals(type);
        }

        /**
         * Prepares the object before it's actually bound to the {@link javax.persistence.Query;}.
         *
         * @param value must not be {@literal null}.
         */
        @Nullable
        public Object prepare(Object value) {
            Assert.notNull(value, "Value must not be null!");

            Class<? extends T> expressionType = expression.getClassType();

            log.debug("Prepare value... type={}, value={}, expressionType={}", type, value, expressionType);

            if (String.class.equals(expressionType)) {

                switch (type) {
                    case STARTING_WITH:
                        return String.format("%s%%", value.toString());
                    case ENDING_WITH:
                        return String.format("%%%s", value.toString());
                    case CONTAINING:
                    case NOT_CONTAINING:
                        return String.format("%%%s%%", value.toString());
                    default:
                        return value;
                }
            }

            return Collection.class.isAssignableFrom(expressionType) //
                   ? persistenceProvider.potentiallyConvertEmptyCollection(toCollection(value)) //
                   : value;
        }

        /**
         * Returns the given argument as {@link Collection} which means it will return it as is if it's a
         * {@link Collections}, turn an array into an {@link ArrayList} or simply wrap any other value into a single element
         * {@link Collections}.
         *
         * @param value the value to be converted to a {@link Collection}.
         * @return the object itself as a {@link Collection} or a {@link Collection} constructed from the value.
         */
        @Nullable
        private static Collection<?> toCollection(@Nullable Object value) {

            if (value == null) {
                return null;
            }

            if (value instanceof Collection) {
                return (Collection<?>) value;
            }

            if (ObjectUtils.isArray(value)) {
                return Arrays.asList(ObjectUtils.toObjectArray(value));
            }

            return Collections.singleton(value);
        }
    }
}
