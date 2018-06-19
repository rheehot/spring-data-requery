package com.coupang.springframework.data.requery.repository.query;

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
import org.springframework.data.support.ExampleMatcherAccessor;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

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
    <E> List<Condition<E, ?>> getConditions(QueryElement<? extends Result<E>> root,
                                            Object exampleValue,
                                            Class<E> probeType,
                                            ExampleMatcherAccessor exampleAccessor) {

        List<Condition<E, ?>> conditions = new ArrayList<>();
        DirectFieldAccessFallbackBeanWrapper beanWrapper = new DirectFieldAccessFallbackBeanWrapper(exampleValue);

        ReflectionUtils.doWithFields(probeType, field -> {

            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            Object fieldValue = beanWrapper.getPropertyValue(fieldName);

            log.trace("Get condition from Example. fieldName={}, fieldType={}, fieldValue={}", fieldName, fieldType, fieldValue);

            NamedExpression<?> expr = NamedExpression.of(fieldName, fieldType);

            if (fieldValue == null) {
                if (exampleAccessor.getNullHandler().equals(NullHandler.INCLUDE)) {
                    Condition<E, ?> condition = (Condition<E, ?>) expr.isNull();
                    conditions.add(condition);
                }
            } else if (fieldType.equals(String.class)) {

                Condition<E, ?> condition;
                NamedExpression<String> stringExpr = (NamedExpression<String>) expr;

                switch (exampleAccessor.getStringMatcherForPath(fieldName)) {
                    case DEFAULT:
                    case EXACT:
                        condition = (Condition<E, ?>) stringExpr.eq((String) fieldValue);
                        break;

                    case CONTAINING:
                        condition = (Condition<E, ?>) stringExpr.like("%" + fieldValue + "%");
                        break;

                    case STARTING:
                        condition = (Condition<E, ?>) stringExpr.like(fieldValue + "%");
                        break;

                    case ENDING:
                        condition = (Condition<E, ?>) stringExpr.like("%" + fieldValue);
                        break;

                    default:
                        throw new IllegalArgumentException("Unsupported StringMatcher " + exampleAccessor.getStringMatcherForPath(fieldName));
                }

                conditions.add(condition);

            } else {
                Condition<E, ?> condition = (Condition<E, ?>) ((NamedExpression) expr).eq(fieldValue);
                conditions.add(condition);
            }
        });

        return conditions;
    }
}

