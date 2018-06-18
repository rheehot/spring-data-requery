package com.coupang.springframework.data.requery.utils;

import io.requery.Key;
import io.requery.meta.QueryAttribute;
import io.requery.query.NamedExpression;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * com.coupang.springframework.data.requery.utils.EntityUtils
 *
 * @author debop
 * @since 18. 6. 18
 */
@Slf4j
@UtilityClass
public class EntityUtils {

    private static Map<Class<?>, NamedExpression<?>> classKeys = new ConcurrentHashMap<>();
    private static NamedExpression<?> UNKNOWN_KEY_EXPRESSION = NamedExpression.of("Unknown", Object.class);

    public NamedExpression<?> getKeyExpression(Class<?> domainClass) {
        log.trace("Retrieve Key property. domainClass={}", domainClass);
        return classKeys
            .computeIfAbsent(domainClass, (domainType) -> {

                final AtomicReference<NamedExpression<?>> keyExprRef = new AtomicReference<>();

                ReflectionUtils.doWithFields(domainType, field -> {
                    if (keyExprRef.get() == null) {
                        if (field.getAnnotation(Key.class) != null) {
                            String keyName = field.getName();
                            Class<?> keyType = field.getType();

                            log.trace("Key field name={}, type={}", keyName, keyType);
                            keyExprRef.set(NamedExpression.of(keyName, keyType));
                        }
                    }
                });

                return keyExprRef.get() != null ? keyExprRef.get() : UNKNOWN_KEY_EXPRESSION;
            });
    }

}
