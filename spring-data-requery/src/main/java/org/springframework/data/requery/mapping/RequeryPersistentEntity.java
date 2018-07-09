package org.springframework.data.requery.mapping;

import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mapping.PersistentEntity;

import java.util.Collection;

/**
 * org.springframework.data.requery.mapping.RequeryPersistentEntity
 *
 * @author debop
 * @since 18. 6. 7
 */
public interface RequeryPersistentEntity<T> extends PersistentEntity<T, RequeryPersistentProperty>, ApplicationContextAware {

    RequeryPersistentProperty getSingleIdProperty();

    Collection<RequeryPersistentProperty> getIdProperties();

    Collection<RequeryPersistentProperty> getIndexes();

    Collection<RequeryPersistentProperty> getEmbeddedProperties();
}
