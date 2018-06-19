package com.coupang.springframework.data.requery.repository.query;

import io.requery.*;
import io.requery.query.Condition;
import io.requery.query.NamedExpression;
import io.requery.query.Result;
import io.requery.query.WhereAndOr;
import io.requery.query.element.QueryElement;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.NullHandler;
import org.springframework.data.domain.ExampleMatcher.PropertySpecifier;
import org.springframework.data.support.ExampleMatcherAccessor;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.StringMatcher;

/**
 * Query by {@link org.springframework.data.domain.Example} 을 수행하기 위해, Example 을 이용하여
 * {@link io.requery.query.WhereAndOr} 를 빌드하도록 합니다.
 *
 * @author debop@coupang.com
 * @since 18. 6. 19
 */
@Slf4j
@UtilityClass
public class QueryByExampleBuilder {

    private static final RequeryFieldFilter requeryFieldFilter = new RequeryFieldFilter();

    /**
     * {@link Example} 를 표현하는 {@link WhereAndOr} 조건절로 빌드합니다.
     */
    @SuppressWarnings("unchecked")
    public static <E> WhereAndOr<? extends Result<E>> getWhereAndOr(QueryElement<? extends Result<E>> root, Example<E> example) {

        Assert.notNull(root, "Root must not be null!");
        Assert.notNull(example, "Example must not be null!");

        ExampleMatcher matcher = example.getMatcher();

        List<Condition<E, ?>> conditions = getConditions(root,
                                                         example.getProbe(),
                                                         example.getProbeType(),
                                                         new ExampleMatcherAccessor(matcher));
        if (conditions.isEmpty()) {
            return (WhereAndOr<? extends Result<E>>) root;
        }
        if (conditions.size() == 1) {
            return root.where(conditions.iterator().next());
        }


        final List<WhereAndOr<? extends Result<E>>> whereClause = new ArrayList<>();
        whereClause.add(root.where(conditions.get(0)));

        conditions.stream()
            .skip(1)
            .forEach(condition -> {
                if (matcher.isAllMatching()) {
                    whereClause.set(0, whereClause.get(0).and(condition));
                } else {
                    whereClause.set(0, whereClause.get(0).or(condition));
                }
            });

        return whereClause.get(0);
    }

    @SuppressWarnings("unchecked")
    private <E> List<Condition<E, ?>> getConditions(QueryElement<? extends Result<E>> root,
                                                    Object exampleValue,
                                                    Class<E> probeType,
                                                    ExampleMatcherAccessor exampleAccessor) {

        List<Condition<E, ?>> conditions = new ArrayList<>();
        DirectFieldAccessFallbackBeanWrapper beanWrapper = new DirectFieldAccessFallbackBeanWrapper(exampleValue);

        ReflectionUtils.doWithFields(probeType, field -> {

            String fieldName = field.getName();
            Class<?> fieldType = field.getType();

            Object fieldValue = beanWrapper.getPropertyValue(fieldName);

            log.trace("Get condition from Example. filed={}, fieldValue={}", field, fieldValue);

            NamedExpression<?> expr = NamedExpression.of(fieldName, fieldType);

            if (fieldValue == null) {
                if (exampleAccessor.getNullHandler().equals(NullHandler.INCLUDE)) {
                    Condition<E, ?> condition = (Condition<E, ?>) expr.isNull();
                    conditions.add(condition);
                }
            } else if (fieldType.equals(String.class)) {
                Condition<E, ?> condition = buildStringCondition(exampleAccessor,
                                                                 (NamedExpression<String>) expr,
                                                                 fieldName,
                                                                 (String) fieldValue);
                conditions.add(condition);

            } else {
                Condition<E, ?> condition = (Condition<E, ?>) ((NamedExpression) expr).eq(fieldValue);
                conditions.add(condition);
            }
        }, requeryFieldFilter); // requeryFieldFilter 를 꼭 지정해주어야 합니다.

        return conditions;
    }

    @SuppressWarnings("unchecked")
    private <E> Condition<E, ?> buildStringCondition(ExampleMatcherAccessor exampleAccessor,
                                                     NamedExpression<String> expression,
                                                     String fieldName,
                                                     String fieldValue) {

        Boolean ignoreCase = false;
        PropertySpecifier specifier = exampleAccessor.getPropertySpecifier(fieldName);
        if (specifier != null) {
            ignoreCase = Optional.ofNullable(specifier.getIgnoreCase()).orElse(false);
        }
        StringMatcher matcher = exampleAccessor.getStringMatcherForPath(fieldName);

        switch (matcher) {
            case DEFAULT:
            case EXACT:
                return (Condition<E, ?>) (ignoreCase
                                          ? expression.function("Lower").eq(((String) fieldValue).toLowerCase())
                                          : expression.eq((String) fieldValue));
            case CONTAINING:
                return (Condition<E, ?>) (ignoreCase
                                          ? expression.function("Lower").like(("%" + fieldValue + "%").toLowerCase())
                                          : expression.like("%" + fieldValue + "%"));
            case STARTING:
                return (Condition<E, ?>) (ignoreCase
                                          ? expression.function("Lower").like((fieldValue + "%").toLowerCase())
                                          : expression.like(fieldValue + "%"));

            case ENDING:
                return (Condition<E, ?>) (ignoreCase
                                          ? expression.function("Lower").like(("%" + fieldValue).toLowerCase())
                                          : expression.like("%" + fieldValue));
            default:
                throw new IllegalArgumentException("Unsupported StringMatcher " + exampleAccessor.getStringMatcherForPath(fieldName));
        }
    }


    /**
     * Example 의 비교 쿼리에 적용하지 말아야 할 Field 를 Filtering 합니다.
     */
    class RequeryFieldFilter implements ReflectionUtils.FieldFilter {

        @Override
        public boolean matches(Field field) {
            if (isTransientField(field))
                return false;
            if (isEmbededField(field)) {
                return false;
            }
            if (isAssociationField(field)) {
                return false;
            }
            if (isRequeryField(field)) {
                return false;
            }

            return true;
        }


        private boolean isTransientField(Field field) {
            return field.isAnnotationPresent(Transient.class);
        }

        private boolean isEmbededField(Field field) {
            return field.isAnnotationPresent(Embedded.class);
        }

        private boolean isAssociationField(Field field) {

            return field.isAnnotationPresent(OneToOne.class) ||
                   field.isAnnotationPresent(OneToMany.class) ||
                   field.isAnnotationPresent(ManyToOne.class) ||
                   field.isAnnotationPresent(ManyToMany.class);
        }

        private boolean isRequeryField(Field field) {
            String fieldName = field.getName();

            return (field.getModifiers() & Modifier.STATIC) > 0 ||
                   "$proxy".equals(fieldName) ||
                   (fieldName.startsWith("$") && fieldName.endsWith("_state"));
        }
    }
}

