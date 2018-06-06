package com.coupang.springframework.data.requery.mapping;

import org.springframework.data.mapping.PersistentEntity;

/**
 * com.coupang.springframework.data.requery.mapping.RequeryPersistentEntity
 *
 * @author debop
 * @since 18. 6. 7
 */
public interface RequeryPersistentEntity<T> extends PersistentEntity<T, RequeryPersistentProperty> {
}
