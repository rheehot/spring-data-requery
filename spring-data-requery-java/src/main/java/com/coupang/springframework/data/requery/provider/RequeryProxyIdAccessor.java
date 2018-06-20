package com.coupang.springframework.data.requery.provider;

import com.coupang.springframework.data.requery.utils.RequeryUtils;
import io.requery.meta.EntityModel;
import io.requery.sql.EntityDataStore;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Persistable;

/**
 * com.coupang.springframework.data.requery.provider.RequeryProxyIdAccessor
 *
 * @author debop
 * @since 18. 6. 7
 */
@Slf4j
public class RequeryProxyIdAccessor implements ProxyIdAccessor {

    private final EntityModel entityModel;

    public RequeryProxyIdAccessor(EntityDataStore entityDataStore) {
        this.entityModel = RequeryUtils.getEntityModel(entityDataStore);
    }

    @Override
    public boolean shouldUseAccessorFor(Object entity) {
        if (entity == null)
            return false;

        log.debug("Is requery entity?. entity={}", entity);

        return entityModel.containsTypeOf(entity.getClass());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @Nullable Object getIdentifierFrom(Object entity) {
        if (entity == null)
            return null;

        log.debug("Get identifier value from entity. entity={}", entity);

        if (Persistable.class.isAssignableFrom(entity.getClass())) {
            return ((Persistable<?>) entity).getId();
        }

        return null;
    }
}
