package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.utils.RequeryUtils;
import io.requery.query.*;
import io.requery.query.element.QueryElement;
import io.requery.query.function.Count;
import io.requery.sql.EntityDataStore;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of the {@link org.springframework.data.repository.CrudRepository} interface.
 * This will offer you a more sophisticated interface than the plain {@link EntityDataStore}.
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@Slf4j
@Repository
@Transactional(readOnly = true)
public class SimpleRequeryRepository<T, ID> implements RequeryRepositoryImplementation<T, ID> {

    @Getter
    private final RequeryOperations operations;
    private final RequeryEntityInformation<T, ID> entityInformation;
    private final Class<T> domainClass;
    private final String domainClassName;

    private CrudMethodMetadata crudMethodMetadata;

    public SimpleRequeryRepository(@NotNull RequeryEntityInformation<T, ID> entityInformation, @NotNull RequeryOperations operations) {
        log.debug("Create SimpleRequeryRepository. domainClass={}", entityInformation.getJavaType());

        this.entityInformation = entityInformation;
        this.domainClass = entityInformation.getJavaType();
        this.domainClassName = domainClass.getSimpleName();

        this.operations = operations;
    }

    @Override
    public void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata) {
        this.crudMethodMetadata = crudMethodMetadata;
    }

    @Transactional
    @Override
    public <S extends T> S upsert(S entity) {
        return operations.upsert(entity);
    }

    @Transactional
    @Override
    public <S extends T> List<S> upsertAll(Iterable<S> entities) {
        return operations.upsertAll(entities);
    }

    @Transactional
    @Override
    public void deleteInBatch(Iterable<T> entities) {
        operations.deleteAll(entities);
    }

    @Transactional
    @Override
    public int deleteAllInBatch() {
        return operations.delete(domainClass).get().value();
    }

    @Override
    public T getOne(ID id) {
        return operations.findById(domainClass, id);
    }

    @NotNull
    @Override
    public List<T> findAll(@NotNull Sort sort) {

        log.debug("Find all {} with sort, sort={}", domainClassName, sort);

        if (sort.isSorted()) {
            OrderingExpression<?>[] orderingExprs = RequeryUtils.getOrderingExpressions(domainClass, sort);

            if (orderingExprs.length > 0) {
                return operations
                    .select(domainClass)
                    .orderBy(orderingExprs)
                    .get()
                    .toList();
            }
        }

        return operations
            .select(domainClass)
            .get()
            .toList();
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public Page<T> findAll(@NotNull Pageable pageable) {

        log.debug("Find all {} with paging, pageable={}", domainClassName, pageable);

        if (pageable.isPaged()) {
            QueryElement<? extends Result<T>> query = (QueryElement<? extends Result<T>>)
                RequeryUtils.applyPageable(domainClass,
                                           (QueryElement<? extends Result<T>>) operations.select(domainClass),
                                           pageable);
            List<T> content = query.get().toList();
            long total = operations.count(domainClass).get().value().longValue();

            return new PageImpl<>(content, pageable, total);
        } else {
            List<T> content = operations
                .select(domainClass)
                .get()
                .toList();
            return new PageImpl<>(content);
        }
    }

    @Transactional
    @NotNull
    @Override
    public <S extends T> S save(@NotNull S entity) {
        return operations.upsert(entity);
    }

    @Transactional
    @NotNull
    @Override
    public <S extends T> List<S> saveAll(@NotNull Iterable<S> entities) {
        return operations.upsertAll(entities);
    }

    @NotNull
    @Override
    public Optional<T> findById(@NotNull ID id) {
        return Optional.ofNullable(operations.findById(domainClass, id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean existsById(@NotNull ID id) {
        NamedExpression<ID> keyExpr = (NamedExpression<ID>) RequeryUtils.getKeyExpression(domainClass);

        Tuple result = operations
            .select(Count.count(domainClass))
            .where(keyExpr.eq(id))
            .get()
            .first();

        return result.<Integer>get(0) > 0;
    }

    @NotNull
    @Override
    public List<T> findAll() {
        return operations.findAll(domainClass);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public List<T> findAllById(@NotNull Iterable<ID> ids) {
        HashSet<ID> idSet = new HashSet<>();
        for (ID id : ids) {
            idSet.add(id);
        }
        NamedExpression<ID> keyExpr = (NamedExpression<ID>) RequeryUtils.getKeyExpression(domainClass);

        return operations
            .select(domainClass)
            .where(keyExpr.in(idSet))
            .get()
            .toList();
    }

    @Override
    public long count() {
        return operations.count(domainClass).get().value().longValue();
    }

    @SuppressWarnings("unchecked")
    @Transactional
    @Override
    public void deleteById(@NotNull ID id) {
        log.debug("Delete {} by id. id={}", domainClassName, id);

        NamedExpression<ID> keyExpr = (NamedExpression<ID>) RequeryUtils.getKeyExpression(domainClass);

        Integer deletedCount = operations
            .delete(domainClass)
            .where(keyExpr.eq(id))
            .get()
            .value();

        log.debug("Deleted count={}", deletedCount);
    }

    @Transactional
    @Override
    public void delete(@NotNull T entity) {
        operations.delete(entity);
    }

    @Transactional
    @Override
    public void deleteAll(@NotNull Iterable<? extends T> entities) {
        operations.deleteAll(entities);
    }

    @Transactional
    @Override
    public void deleteAll() {
        log.debug("Delete All {} ...", domainClassName);

        Integer deletedCount = operations.delete(domainClass).get().value();

        log.debug("Delete All {}, deleted count={}", domainClassName, deletedCount);
    }

    @NotNull
    @Override
    public <S extends T> Optional<S> findOne(@NotNull Example<S> example) {
        throw new NotImplementedException("구현 중");
    }

    @NotNull
    @Override
    public <S extends T> List<S> findAll(@NotNull Example<S> example) {
        throw new NotImplementedException("구현 중");
    }

    @NotNull
    @Override
    public <S extends T> List<S> findAll(@NotNull Example<S> example, @NotNull Sort sort) {
        throw new NotImplementedException("구현 중");
    }


    @NotNull
    @Override
    public <S extends T> Page<S> findAll(@NotNull Example<S> example, @NotNull Pageable pageable) {
        throw new NotImplementedException("구현 중");
    }

    @Override
    public <S extends T> long count(@NotNull Example<S> example) {
        throw new NotImplementedException("구현 중");
    }

    @Override
    public <S extends T> boolean exists(@NotNull Example<S> example) {
        throw new NotImplementedException("구현 중");
    }

    @Override
    public Optional<T> findOne(Return<? extends Result<T>> whereClause) {
        return Optional.ofNullable(whereClause.get().firstOrNull());
    }

    @Override
    public List<T> findAll(Return<? extends Result<T>> whereClause) {
        return whereClause.get().toList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Page<T> findAll(QueryElement<? extends Result<T>> whereClause, Pageable pageable) {

        int total = count(whereClause);

        Return<?> query = RequeryUtils.applyPageable(domainClass, whereClause, pageable);
        List<T> contents = ((Return<? extends Result<T>>) query).get().toList();

        return new PageImpl<>(contents, pageable, total);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll(Iterable<Condition<T, ?>> conditions, Sort sort) {

        QueryElement<? extends Result<T>> baseQuery = (QueryElement<? extends Result<T>>) getOperations().select(domainClass);
        baseQuery = (QueryElement<? extends Result<T>>) RequeryUtils
            .buildWhereClause(baseQuery,
                              RequeryUtils.getGenericConditions(conditions),
                              true);

        Return<? extends Result<T>> query = (Return<? extends Result<T>>) RequeryUtils.applySort(domainClass, baseQuery, sort);

        return query.get().toList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll(Iterable<Condition<T, ?>> conditions) {
        QueryElement<?> baseQuery = (QueryElement<?>) getOperations().select(domainClass);


        Return<? extends Result<T>> query = (Return<? extends Result<T>>)
            RequeryUtils.buildWhereClause(baseQuery, RequeryUtils.getGenericConditions(conditions), true);

        return query.get().toList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int count(QueryElement<? extends Result<T>> whereClause) {
        return getOperations().count(domainClass, whereClause);
    }

    @Override
    public boolean exists(QueryElement<? extends Result<T>> whereClause) {
        return getOperations().exists(domainClass, whereClause);
    }


}
