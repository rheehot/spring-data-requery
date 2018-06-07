package com.coupang.springframework.data.requery.repository.support;

import io.requery.meta.EntityModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Persistable;

/**
 * Extension of {@link RequeryEntityModelEntityInformation} that consideres methods of {@link Persistable} to lookup the id.
 *
 * @author debop@coupang.com
 * @since 18. 6. 7
 */
public class RequeryPersistableEntityInformation<T extends Persistable<ID>, ID>
    extends RequeryEntityModelEntityInformation<T, ID> {

    public RequeryPersistableEntityInformation(@NotNull Class<T> domainClass,
                                               @NotNull EntityModel entityModel) {
        super(domainClass, entityModel);
    }

    @Override
    public boolean isNew(T entity) {
        return entity.isNew();
    }

    @Override
    public ID getId(T entity) {
        return entity.getId();
    }
}
