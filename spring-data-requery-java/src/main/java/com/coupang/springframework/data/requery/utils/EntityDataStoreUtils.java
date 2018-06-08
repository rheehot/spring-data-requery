package com.coupang.springframework.data.requery.utils;

import io.requery.meta.Attribute;
import io.requery.meta.EntityModel;
import io.requery.meta.Type;
import io.requery.sql.EntityContext;
import io.requery.sql.EntityDataStore;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * EntityDataStoreUtils
 *
 * @author debop@coupang.com
 * @since 18. 6. 7
 */
@Slf4j
public final class EntityDataStoreUtils {

    private EntityDataStoreUtils() {}

    @NotNull
    public static <T> EntityContext getEntityContext(@NotNull EntityDataStore entityDataStore) {
        try {
            Field f = entityDataStore.getClass().getDeclaredField("context");
            f.setAccessible(true);
            EntityContext entityContext = (EntityContext) f.get(entityDataStore);
            log.debug("Get EntityContext. entityContext={}", entityContext);
            return entityContext;
        } catch (Exception e) {
            throw new IllegalStateException("Fail to retrieve EntityContext.");
        }
    }

    @NotNull
    public static EntityModel getEntityModel(@NotNull EntityDataStore entityDataStore) {
        try {
            Field f = entityDataStore.getClass().getDeclaredField("entityModel");
            f.setAccessible(true);
            EntityModel entityModel = (EntityModel) f.get(entityDataStore);
            log.debug("Get EntityModel. entityModel name={}", entityModel.getName());
            return entityModel;
        } catch (Exception e) {
            throw new IllegalStateException("Fail to retrieve entity model.");
        }
    }

    @NotNull
    public static Set<Type<?>> getEntityTypes(@NotNull EntityDataStore entityDataStore) {

        EntityModel model = getEntityModel(entityDataStore);
        Assert.notNull(model, "Not found EntityModel!");

        return model.getTypes();
    }

    @NotNull
    public static List<Class<?>> getEntityClasses(@NotNull EntityDataStore entityDataStore) {

        return getEntityTypes(entityDataStore).stream()
            .map(Type::getClassType)
            .collect(Collectors.toList());
    }

    @Nullable
    public static Type<?> getType(@NotNull EntityDataStore entityDataStore, @NotNull Class<?> entityClass) {

        Set<Type<?>> types = getEntityTypes(entityDataStore);

        return types.stream()
            .filter(it -> entityClass.equals(it.getClassType()))
            .findFirst()
            .orElse(null);
    }

    public static Set<? extends Attribute<?, ?>> getIdAttribute(@NotNull EntityDataStore entityDataStore, @NotNull Class<?> entityClass) {
        Type<?> type = getType(entityDataStore, entityClass);
        return (type != null) ? type.getKeyAttributes() : new HashSet<>();
    }

    @Nullable
    public static Attribute<?, ?> getSingleIdAttribute(@NotNull EntityDataStore entityDataStore, @NotNull Class<?> entityClass) {
        Type<?> type = getType(entityDataStore, entityClass);
        return (type != null) ? type.getSingleKeyAttribute() : null;
    }
}
