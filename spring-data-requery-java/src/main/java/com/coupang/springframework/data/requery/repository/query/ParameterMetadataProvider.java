package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.provider.RequeryPersistenceProvider;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.ParameterExpression;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Helper class to allow easy creation of {@link ParameterMetadata}s.
 *
 * @author debop
 * @since 18. 6. 14
 */
public class ParameterMetadataProvider {


    /**
     * @param <T>
     * @author Oliver Gierke
     * @author Thomas Darimont
     */
    static class ParameterMetadata<T> {

        static final Object PLACEHOLDER = new Object();

        private final Part.Type type;
        private final ParameterExpression<T> expression;
        private final RequeryPersistenceProvider persistenceProvider;

        /**
         * Creates a new {@link ParameterMetadata}.
         */
        public ParameterMetadata(ParameterExpression<T> expression,
                                 Part.Type type,
                                 @Nullable Object value,
                                 RequeryPersistenceProvider provider) {

            this.expression = expression;
            this.persistenceProvider = provider;
            this.type = value == null && Part.Type.SIMPLE_PROPERTY.equals(type) ? Part.Type.IS_NULL : type;
        }

        /**
         * Returns the {@link ParameterExpression}.
         *
         * @return the expression
         */
        public ParameterExpression<T> getExpression() {
            return expression;
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

            Class<? extends T> expressionType = expression.getJavaType();

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
