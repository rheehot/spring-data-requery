package org.springframework.data.requery.repository;

import io.requery.sql.EntityDataStore;

/**
 * Interface for components to provide useful information about the current Requery setup within the current
 * {@link org.springframework.context.ApplicationContext}.
 *
 * @author debop@coupang.com
 * @since 18. 6. 7
 */
@Deprecated
public interface RequeryContext {

    /**
     * Returns the {@link EntityDataStore} managing the given domain type.
     *
     * @param managedType must not be {@literal null}.
     * @return the {@link EntityDataStore} that manages the given type, will never be {@literal null}.
     * @throws IllegalArgumentException if the given type is not a Requery managed one no unique {@link EntityDataStore} managing this type can be resolved.
     */
    EntityDataStore<Object> getEntityDataStoreByManagedType(Class<?> managedType);
}
