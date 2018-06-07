package com.coupang.springframework.data.requery.repository.query;

import org.springframework.data.repository.core.EntityMetadata;

/**
 * Requery specific extension of {@link EntityMetadata}.
 *
 * @author debop@coupang.com
 * @since 18. 6. 7
 */
public interface RequeryEntityMetadata<T> extends EntityMetadata<T> {

    /**
     * Returns the name of the entity
     *
     * @return
     */
    String getEntityName();


    String getModelName();
}
