package com.coupang.springframework.data.requery.core;

import com.coupang.springframework.data.requery.mapping.RequeryMappingContext;
import com.coupang.springframework.data.requery.utils.EntityDataStoreUtils;
import io.requery.TransactionIsolation;
import io.requery.meta.Attribute;
import io.requery.meta.EntityModel;
import io.requery.meta.QueryAttribute;
import io.requery.query.*;
import io.requery.sql.EntityContext;
import io.requery.sql.EntityDataStore;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * Java용 RequeryOperations
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
public interface RequeryOperations {

    EntityDataStore<Object> getDataStore();

    RequeryMappingContext getMappingContext();

    default EntityModel getEntityModel() {
        return EntityDataStoreUtils.getEntityModel(getDataStore());
    }

    @SuppressWarnings("unchecked")
    default <E> EntityContext<E> getEntityContext() {
        return EntityDataStoreUtils.getEntityContext(getDataStore());
    }

    default <E> Selection<? extends Result<E>> select(Class<E> entityType) {
        return getDataStore().select(entityType);
    }

    default <E> Selection<? extends Result<E>> select(Class<E> entityType, QueryAttribute<?, ?>... attributes) {
        return getDataStore().select(entityType, attributes);
    }

    default Selection<? extends Result<Tuple>> select(Expression<?>... expressions) {
        return getDataStore().select(expressions);
    }

    default <E, K> E findById(Class<E> entityType, K id) {
        return getDataStore().findByKey(entityType, id);
    }

    default <E> Iterable<E> findAll(Class<E> entityType) {
        return getDataStore().select(entityType).get().toList();
    }

    default <E> E refresh(@NotNull E entity) {
        return getDataStore().refresh(entity);
    }

    default <E> E refresh(@NotNull E entity, Attribute<?, ?>... attributes) {
        return getDataStore().refresh(entity, attributes);
    }

    default <E> Iterable<E> refresh(@NotNull Iterable<E> entities, Attribute<?, ?>... attributes) {
        return getDataStore().refresh(entities, attributes);
    }

    default <E> E refreshAll(@NotNull E entity) {
        return getDataStore().refreshAll(entity);
    }

    default <E> E upsert(@NotNull E entity) {
        return getDataStore().upsert(entity);
    }

    default <E> Iterable<E> upsertAll(@NotNull Iterable<E> entities) {
        return getDataStore().upsert(entities);
    }

    default <E> E insert(@NotNull E entity) {
        return getDataStore().insert(entity);
    }

    default <E, K> K insert(@NotNull E entity, Class<K> keyClass) {
        return getDataStore().insert(entity, keyClass);
    }

    default <E> Insertion<? extends Result<Tuple>> insert(Class<E> entityType) {
        return getDataStore().insert(entityType);
    }

    default <E> InsertInto<? extends Result<Tuple>> insert(Class<E> entityType, QueryAttribute<E, ?>... attributes) {
        return getDataStore().insert(entityType, attributes);
    }

    default <E> Iterable<E> insertAll(@NotNull Iterable<E> entities) {
        return getDataStore().insert(entities);
    }

    default <E, K> Iterable<K> insertAll(@NotNull Iterable<E> entities, Class<K> keyClass) {
        return getDataStore().insert(entities, keyClass);
    }


    default Update<? extends Scalar<Integer>> update() {
        return getDataStore().update();
    }

    default <E> E update(@NotNull E entity) {
        return getDataStore().update(entity);
    }

    default <E> E update(@NotNull E entity, Attribute<?, ?>... attributes) {
        return getDataStore().update(entity, attributes);
    }

    default <E> Update<? extends Scalar<Integer>> update(Class<E> entityType) {
        return getDataStore().update(entityType);
    }

    default <E> Iterable<E> updateAll(@NotNull Iterable<E> entities) {
        return getDataStore().update(entities);
    }

    default <E> Deletion<? extends Scalar<Integer>> delete() {
        return getDataStore().delete();
    }

    default <E> Deletion<? extends Scalar<Integer>> delete(Class<E> entityType) {
        return getDataStore().delete(entityType);
    }

    default <E> void delete(E entity) {
        getDataStore().delete(entity);
    }

    default <E> void deleteAll(Iterable<E> entities) {
        getDataStore().delete(entities);
    }

    default <E> Integer deleteAll(Class<E> entityType) {
        return getDataStore().delete(entityType).get().value();
    }

    default <E> Selection<? extends Scalar<Integer>> count(Class<E> entityType) {
        return getDataStore().count(entityType);
    }

    default <E> Selection<? extends Scalar<Integer>> count(QueryAttribute<?, ?>... attributes) {
        return getDataStore().count(attributes);
    }

    default Result<Tuple> raw(String query, Object... parameters) {
        return getDataStore().raw(query, parameters);
    }

    default <E> Result<E> raw(Class<E> entityType, String query, Object... parameters) {
        return getDataStore().raw(entityType, query, parameters);
    }

    default <V> V runInTransaction(Callable<V> callable) {
        return runInTransaction(callable, null);
    }

    <V> V runInTransaction(Callable<V> callable, @Nullable TransactionIsolation isolation);

    default <V> V withTransaction(Function<EntityDataStore<Object>, V> block) {
        return withTransaction(block, null);
    }

    <V> V withTransaction(Function<EntityDataStore<Object>, V> block, @Nullable TransactionIsolation isolation);
}
