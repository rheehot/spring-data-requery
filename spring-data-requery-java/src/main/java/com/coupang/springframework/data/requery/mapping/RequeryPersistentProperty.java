package com.coupang.springframework.data.requery.mapping;

import org.springframework.data.mapping.PersistentProperty;

/**
 * com.coupang.springframework.data.requery.mapping.RequeryPersistentProperty
 *
 * @author debop
 * @since 18. 6. 7
 */
public interface RequeryPersistentProperty extends PersistentProperty<RequeryPersistentProperty> {

    boolean isIdProperty();

    boolean isEmbedded();

    boolean hasIndex();

    String getFieldName();

}
