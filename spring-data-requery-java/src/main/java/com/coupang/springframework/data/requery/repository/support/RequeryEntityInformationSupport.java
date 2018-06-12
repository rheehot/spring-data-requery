package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.repository.query.DefaultRequeryEntityMetadata;
import com.coupang.springframework.data.requery.repository.query.RequeryEntityMetadata;
import com.coupang.springframework.data.requery.utils.EntityDataStoreUtils;
import io.requery.meta.EntityModel;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.core.support.AbstractEntityInformation;
import org.springframework.util.Assert;

/**
 * RequeryEntityInformationSupport
 *
 * @author debop@coupang.com
 * @since 18. 6. 7
 */
@Slf4j
public abstract class RequeryEntityInformationSupport<T, ID>
    extends AbstractEntityInformation<T, ID> implements RequeryEntityInformation<T, ID> {

    private RequeryEntityMetadata<T> metadata;

    public RequeryEntityInformationSupport(Class<T> domainClass) {
        super(domainClass);
        this.metadata = new DefaultRequeryEntityMetadata(domainClass);
    }

    public String getEntityName() {
        return metadata.getEntityName();
    }

    public String getModelName() {
        return metadata.getModelName();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> RequeryEntityInformation<T, ?> getEntityInformation(@NotNull Class<T> domainClass,
                                                                          @NotNull RequeryOperations operations) {
        Assert.notNull(domainClass, "Domain class must not be null.");
        Assert.notNull(operations, "RequeryOperations must not be null.");

        EntityModel entityModel = operations.getEntityModel();
        Assert.notNull(entityModel, "EntityModel must not be null!");
        log.debug("entityModel={}", entityModel);

        if (org.springframework.data.domain.Persistable.class.isAssignableFrom(domainClass)) {
            return new RequeryPersistableEntityInformation(domainClass, entityModel);
        } else {
            return new RequeryEntityModelEntityInformation<>(domainClass, entityModel);
        }
    }
}
