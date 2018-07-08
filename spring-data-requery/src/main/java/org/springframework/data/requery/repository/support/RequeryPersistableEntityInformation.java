package org.springframework.data.requery.repository.support;

import io.requery.meta.EntityModel;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Persistable;
import org.springframework.util.Assert;

/**
 * Extension of {@link RequeryEntityModelEntityInformation} that consideres methods of {@link Persistable} to lookup the id.
 *
 * @author debop@coupang.com
 * @since 18. 6. 7
 */
@Slf4j
public class RequeryPersistableEntityInformation<T extends Persistable<ID>, ID>
    extends RequeryEntityModelEntityInformation<T, ID> {

    public RequeryPersistableEntityInformation(@NotNull Class<T> domainClass,
                                               @NotNull EntityModel entityModel) {
        super(domainClass, entityModel);
    }

    @Override
    public boolean isNew(@NotNull T entity) {
        Assert.notNull(entity, "entity must not be nulll");
        log.trace("is new entity. entity={}", entity);
        return entity.isNew();
    }

    @Override
    public ID getId(@NotNull T entity) {
        Assert.notNull(entity, "entity must not be nulll");
        log.trace("get id of entity. entity={}", entity);
        return entity.getId();
    }
}
