package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.utils.RequeryUtils;
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
import org.springframework.util.LinkedMultiValueMap;

import java.lang.reflect.Field;
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

    private static final LinkedMultiValueMap<Class<?>, Field> entityFields = new LinkedMultiValueMap<>();

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

        return RequeryUtils.buildWhereClause(root, conditions, matcher.isAllMatching());
    }

    @SuppressWarnings("unchecked")
    private <E> List<Condition<E, ?>> getConditions(QueryElement<? extends Result<E>> root,
                                                    Object exampleValue,
                                                    Class<E> probeType,
                                                    ExampleMatcherAccessor exampleAccessor) {

        List<Condition<E, ?>> conditions = new ArrayList<>();
        DirectFieldAccessFallbackBeanWrapper beanWrapper = new DirectFieldAccessFallbackBeanWrapper(exampleValue);

        List<Field> fields = RequeryUtils.findEntityFields(probeType);

        for (Field field : fields) {
            // Query By Example 에서 지원하지 못하는 Field 들은 제외합니다.
            boolean notSupportedField = RequeryUtils.isAssociationField(field) ||
                                        RequeryUtils.isEmbededField(field) ||
                                        RequeryUtils.isTransientField(field);
            if (notSupportedField) {
                continue;
            }

            log.trace("Build condition... field={}", field);

            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            Object fieldValue = beanWrapper.getPropertyValue(fieldName);

            log.trace("Get condition from Example. filed={}, fieldValue={}", field, fieldValue);

            NamedExpression<?> expr = NamedExpression.of(fieldName, fieldType);

            if (fieldValue == null) {
                if (exampleAccessor.getNullHandler().equals(NullHandler.INCLUDE)) {
                    conditions.add((Condition<E, ?>) expr.isNull());
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
        }

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
}

