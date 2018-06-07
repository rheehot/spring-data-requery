package com.coupang.springframework.data.requery.utils;

import io.requery.meta.EntityModel;
import io.requery.meta.Type;
import io.requery.sql.EntityDataStore;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.List;
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

    @Nullable
    public static EntityModel getEntityModel(@NotNull EntityDataStore entityDataStore) {
        try {
            Field f = entityDataStore.getClass().getDeclaredField("entityModel");
            f.setAccessible(true);
            EntityModel entityModel = (EntityModel) f.get(entityDataStore);
            log.debug("Get EntityModel. entityModel name={}", entityModel.getName());
            return entityModel;
        } catch (Exception e) {
            log.warn("Fail to retrive entity model.", e);
            return null;
        }
    }

    @NotNull
    public static List<Class<?>> getEntityClasses(@NotNull EntityDataStore entityDataStore) {
        EntityModel model = getEntityModel(entityDataStore);

        Assert.notNull(model, "Not found EntityModel!");

        return model.getTypes()
            .stream()
            .map(Type::getClassType)
            .collect(Collectors.toList());
    }
}
