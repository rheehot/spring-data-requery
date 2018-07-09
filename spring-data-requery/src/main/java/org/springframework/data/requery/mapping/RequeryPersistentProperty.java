package org.springframework.data.requery.mapping;

import org.springframework.data.mapping.PersistentProperty;

/**
 * org.springframework.data.requery.mapping.RequeryPersistentProperty
 *
 * @author debop
 * @since 18. 6. 7
 */
public interface RequeryPersistentProperty extends PersistentProperty<RequeryPersistentProperty> {

    boolean isIdProperty();

    boolean isTransient();

    boolean isAssociation();

    boolean isEmbedded();

    boolean hasIndex();

    String getFieldName();

}
