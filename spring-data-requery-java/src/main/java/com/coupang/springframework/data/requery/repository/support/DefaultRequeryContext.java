package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.repository.RequeryContext;
import com.coupang.springframework.data.requery.utils.EntityDataStoreUtils;
import io.requery.sql.EntityDataStore;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Set;

/**
 * Default implementation of {@link RequeryContext}.
 *
 * @author debop@coupang.com
 * @since 18. 6. 7
 */
@Slf4j
public class DefaultRequeryContext implements RequeryContext {

    private final MultiValueMap<Class<?>, EntityDataStore> entityDataStores;

    public DefaultRequeryContext(@NotNull Set<EntityDataStore> entityDataStores) {
        Assert.notEmpty(entityDataStores, "entityDataStores must not be empty!");

        this.entityDataStores = new LinkedMultiValueMap<>();
        for (EntityDataStore dataStore : entityDataStores) {
            List<Class<?>> classes = EntityDataStoreUtils.getEntityClasses(dataStore);
            for (Class<?> clazz : classes) {
                this.entityDataStores.add(clazz, dataStore);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public EntityDataStore getEntityDataStoreByManagedType(@NotNull Class<?> managedType) {

        if (!entityDataStores.containsKey(managedType)) {
            throw new IllegalArgumentException(managedType + " is not a managed type!");
        }

        List<EntityDataStore> candidates = this.entityDataStores.get(managedType);

        if (candidates.size() == 1) {
            return candidates.get(0);
        }

        throw new IllegalArgumentException(
            String.format("%s managed by more than one EntityManagers: %s!", managedType.getName(), candidates)
        );
    }
}
