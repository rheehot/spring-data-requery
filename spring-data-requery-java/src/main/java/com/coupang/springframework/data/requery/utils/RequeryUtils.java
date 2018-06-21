package com.coupang.springframework.data.requery.utils;

import io.requery.*;
import io.requery.meta.Attribute;
import io.requery.meta.EntityModel;
import io.requery.meta.Type;
import io.requery.query.*;
import io.requery.query.element.QueryElement;
import io.requery.sql.EntityContext;
import io.requery.sql.EntityDataStore;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * com.coupang.springframework.data.requery.utils.RequeryUtils
 *
 * @author debop
 * @since 18. 6. 20
 */
@Slf4j
@UtilityClass
public class RequeryUtils {

    private static Map<Class<?>, NamedExpression<?>> classKeys = new ConcurrentHashMap<>();
    private static NamedExpression<?> UNKNOWN_KEY_EXPRESSION = NamedExpression.of("Unknown", Object.class);

    public NamedExpression<?> getKeyExpression(@NotNull Class<?> domainClass) {
        Assert.notNull(domainClass, "domainClass must not be null!");
        log.trace("Retrieve Key property. domainClass={}", domainClass.getSimpleName());

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

    @SuppressWarnings("ConstantConditions")
    public static <T> EntityContext getEntityContext(EntityDataStore entityDataStore) {
        Assert.notNull(entityDataStore, "entityDataStore must not be null!");

        try {
            Field f = ReflectionUtils.findField(entityDataStore.getClass(), "context");
            Assert.notNull(f, "context field must not be null!");


            f.setAccessible(true);
            return (EntityContext) ReflectionUtils.getField(f, entityDataStore);
        } catch (Exception e) {
            throw new IllegalStateException("Fail to retrieve EntityContext.");
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static EntityModel getEntityModel(@NotNull EntityDataStore entityDataStore) {
        Assert.notNull(entityDataStore, "entityDataStore must not be null!");

        try {
            Field f = ReflectionUtils.findField(entityDataStore.getClass(), "entityModel");
            Assert.notNull(f, "entityModel field must not be null!");

            f.setAccessible(true);
            return (EntityModel) ReflectionUtils.getField(f, entityDataStore);
        } catch (Exception e) {
            throw new IllegalStateException("Fail to retrieve entity model.", e);
        }
    }

    @NotNull
    public static Set<Type<?>> getEntityTypes(@NotNull EntityDataStore entityDataStore) {
        Assert.notNull(entityDataStore, "entityDataStore must not be null!");

        EntityModel model = getEntityModel(entityDataStore);
        Assert.notNull(model, "Not found EntityModel!");

        return model.getTypes();
    }

    @NotNull
    public static List<Class<?>> getEntityClasses(@NotNull EntityDataStore entityDataStore) {
        Assert.notNull(entityDataStore, "entityDataStore must not be null!");

        return getEntityTypes(entityDataStore).stream()
            .map(Type::getClassType)
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <E> Type<E> getType(@NotNull EntityDataStore entityDataStore, @NotNull Class<E> entityClass) {

        Assert.notNull(entityDataStore, "entityDataStore must not be null!");
        Assert.notNull(entityClass, "entityClass must not be null!");

        Set<Type<?>> types = getEntityTypes(entityDataStore);

        return (Type<E>) types
            .stream()
            .filter(it -> entityClass.equals(it.getClassType()))
            .findFirst()
            .orElse(null);
    }

    public static <E> Set<? extends Attribute<E, ?>> getKeyAttributes(@NotNull EntityDataStore entityDataStore,
                                                                      @NotNull Class<E> entityClass) {
        Assert.notNull(entityDataStore, "entityDataStore must not be null!");
        Assert.notNull(entityClass, "entityClass must not be null!");

        Type<E> type = getType(entityDataStore, entityClass);
        return (type != null) ? type.getKeyAttributes() : Collections.emptySet();
    }

    @Nullable
    public static <E> Attribute<E, ?> getSingleKeyAttribute(@NotNull EntityDataStore entityDataStore,
                                                            @NotNull Class<E> entityClass) {
        Assert.notNull(entityDataStore, "entityDataStore must not be null!");
        Assert.notNull(entityClass, "entityClass must not be null!");

        Type<E> type = getType(entityDataStore, entityClass);
        return (type != null) ? type.getSingleKeyAttribute() : null;
    }


    @SuppressWarnings("unchecked")
    public static QueryElement<? extends Result<?>> applyPageable(@NotNull Class<?> domainClass,
                                                                  @NotNull QueryElement<? extends Result<?>> baseQuery,
                                                                  @NotNull Pageable pageable) {
        Assert.notNull(domainClass, "domainClass must not be null!");
        Assert.notNull(baseQuery, "baseQuery must not be null!");
        Assert.notNull(pageable, "pageable must not be null!");

        log.trace("Apply paging, domainClass={}, pageable={}", domainClass.getSimpleName(), pageable);

        if (pageable.isUnpaged()) {
            return baseQuery;
        }

        QueryElement<? extends Result<?>> query = applySort(domainClass, baseQuery, pageable.getSort());

        return (QueryElement<? extends Result<?>>) query
            .limit(pageable.getPageSize())
            .offset((int) pageable.getOffset());
    }

    @SuppressWarnings("unchecked")
    public static QueryElement<? extends Result<?>> applySort(@NotNull Class<?> domainClass,
                                                              @NotNull QueryElement<? extends Result<?>> baseQuery,
                                                              @NotNull Sort sort) {
        log.trace("Apply sort, domainClass={}, sort={}", domainClass.getSimpleName(), sort);

        if (sort.isUnsorted()) {
            return baseQuery;
        }

        for (Sort.Order order : sort) {
            String propertyName = order.getProperty();
            Sort.Direction direction = order.getDirection();

            Field field = findField(domainClass, propertyName);
            if (field != null) {
                NamedExpression<?> expr = NamedExpression.of(propertyName, field.getType());

                baseQuery = (QueryElement<? extends Result<?>>) baseQuery.orderBy(direction.isAscending() ? expr.asc() : expr.desc());
            }
        }

        return baseQuery;
    }

    @NotNull
    public static OrderingExpression<?>[] getOrderingExpressions(Class<?> domainClass, Sort sort) {
        if (sort == null || sort.isUnsorted()) {
            return new OrderingExpression[0];
        }

        List<OrderingExpression<?>> orderingExprs = new ArrayList<>();

        for (Sort.Order order : sort) {
            String propertyName = order.getProperty();
            Sort.Direction direction = order.getDirection();

            Field field = findField(domainClass, propertyName);
            if (field != null) {
                NamedExpression<?> expr = NamedExpression.of(propertyName, field.getType());
                OrderingExpression<?> orderingExpr = (order.isAscending()) ? expr.asc() : expr.desc();
                orderingExprs.add(orderingExpr);
            }
        }

        return orderingExprs.toArray(new OrderingExpression<?>[0]);
    }

    @SuppressWarnings("unchecked")
    public static <E> WhereAndOr<? extends Result<E>> buildWhereClause(QueryElement<? extends Result<E>> baseQuery,
                                                                       Iterable<Condition<E, ?>> conditions,
                                                                       boolean isAnd) {

        List<Condition<E, ?>> conds = Iterables.toList(conditions);

        if (conds.isEmpty()) {
            return (WhereAndOr<? extends Result<E>>) baseQuery;
        } else if (conds.size() == 1) {
            return baseQuery.where(conds.get(0));
        } else {

            final List<WhereAndOr<? extends Result<E>>> whereClauses = new ArrayList<>();
            whereClauses.add(baseQuery.where(conds.get(0)));

            conds.stream()
                .skip(1)
                .forEach(condition -> {
                    if (isAnd) {
                        whereClauses.set(0, whereClauses.get(0).and(condition));
                    } else {
                        whereClauses.set(0, whereClauses.get(0).or(condition));
                    }
                });

            return whereClauses.get(0);
        }
    }

    /**
     * Cache for Field of Class
     */
    private static Map<String, Field> classFieldCache = new ConcurrentHashMap<>();

    /**
     * 지정된 클래스의 특정 필드명을 가진 {@link Field} 정보를 가져온다. 없다면 null 반환
     *
     * @param domainClass 대상 클래스 수형
     * @param fieldName   필드명
     * @return {@link Field} 정보를 가져온다. 없다면 null 반환
     */
    @Nullable
    public static Field findField(@NotNull final Class<?> domainClass, @NotNull final String fieldName) {

        Assert.notNull(domainClass, "domainClass must not be null!");
        Assert.hasText(fieldName, "fieldName must not be empty!");

        String cacheKey = domainClass.getName() + "." + fieldName;

        return classFieldCache.computeIfAbsent(cacheKey, key -> {
            Class<?> targetClass = domainClass;

            do {
                try {
                    Field foundField = targetClass.getDeclaredField(fieldName);
                    if (foundField != null) {
                        return foundField;
                    }
                } catch (Exception e) {
                    // Nothing to do.
                }
                targetClass = targetClass.getSuperclass();
            } while (targetClass != null && targetClass != Object.class);

            return null;
        });
    }

    /**
     * 지정된 클래스에서 원하는 Field 정보만을 가져온다.
     *
     * @param domainClass    대상 Class 정보
     * @param fieldPredicate 원하는 {@link Field} 인지 판단하는 predicate
     * @return 조건에 맞는 Field 정보
     */
    public static List<Field> findFields(@NotNull final Class<?> domainClass,
                                         @NotNull final Predicate<Field> fieldPredicate) {
        Assert.notNull(domainClass, "domainClass must not be null!");
        Assert.notNull(fieldPredicate, "predicate must not be null!");

        List<Field> foundFields = new ArrayList<>();
        Class<?> targetClass = domainClass;

        do {
            Field[] fields = targetClass.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    if (fieldPredicate.test(field)) {
                        foundFields.add(field);
                    }
                }
            }
            targetClass = targetClass.getSuperclass();
        } while (targetClass != null && targetClass != Object.class);

        return foundFields;
    }

    /**
     *
     */
    private static MultiValueMap<Class<?>, Field> entityFields = new LinkedMultiValueMap<>();

    /**
     * Requery Entity 에서 독립적인 컬럼 역할을 수행하는 Field 만 가져옵니다.
     *
     * @param domainClass Requery 엔티티의 클래스
     * @return Field collection
     */
    @NotNull
    public static List<Field> findEntityFields(@NotNull final Class<?> domainClass) {
        return entityFields.computeIfAbsent(domainClass, clazz ->
            findFields(clazz, RequeryUtils::isRequeryEntityField)
        );
    }

    public static boolean isRequeryEntityField(Field field) {
        return !isRequeryGeneratedField(field);
    }

    public boolean isRequeryGeneratedField(Field field) {
        String fieldName = field.getName();

        return (field.getModifiers() & Modifier.STATIC) > 0 ||
               "$proxy".equals(fieldName) ||
               (fieldName.startsWith("$") && fieldName.endsWith("_state"));
    }

    public static boolean isTransientField(Field field) {
        return field.isAnnotationPresent(Transient.class);
    }

    public static boolean isEmbededField(Field field) {
        return field.isAnnotationPresent(Embedded.class);
    }

    public static boolean isAssociationField(Field field) {
        return field.isAnnotationPresent(OneToOne.class) ||
               field.isAnnotationPresent(OneToMany.class) ||
               field.isAnnotationPresent(ManyToOne.class) ||
               field.isAnnotationPresent(ManyToMany.class);
    }

}
