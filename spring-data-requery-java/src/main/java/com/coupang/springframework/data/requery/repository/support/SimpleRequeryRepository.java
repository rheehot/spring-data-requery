package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import io.requery.query.WhereAndOr;
import io.requery.sql.EntityDataStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    private final RequeryOperations operations;
    private final RequeryEntityInformation<T, ID> entityInformation;
    private final Class<T> domainType;

    public SimpleRequeryRepository(@NotNull RequeryEntityInformation<T, ID> entityInformation, @NotNull RequeryOperations operations) {
        log.debug("Create SimpleRequeryRepository. domainType={}", entityInformation.getJavaType());

        this.entityInformation = entityInformation;
        this.domainType = entityInformation.getJavaType();
        this.operations = operations;
    }

    @Override
    public <S extends T> S upsert(S entity) {
        return operations.upsert(entity);
    }

    @Override
    public <S extends T> Iterable<S> upsertAll(Iterable<S> entities) {
        return operations.upsertAll(entities);
    }

    @Override
    public <S extends T> void deleteInBatch(Iterable<S> entities) {
        operations.deleteAll(entities);
    }

    @Override
    public int deleteAllInBatch() {
        return operations.delete(domainType).get().value();
    }

    @Override
    public T getOne(ID id) {
        return operations.findById(domainType, id);
    }

    @NotNull
    @Override
    public Iterable<T> findAll(@NotNull Sort sort) {
        throw new NotImplementedException("구현 중");
    }

    @NotNull
    @Override
    public Page<T> findAll(@NotNull Pageable pageable) {
        throw new NotImplementedException("구현 중");
    }

    @NotNull
    @Override
    public <S extends T> S save(@NotNull S entity) {
        return operations.upsert(entity);
    }

    @NotNull
    @Override
    public <S extends T> Iterable<S> saveAll(@NotNull Iterable<S> entities) {
        return operations.upsertAll(entities);
    }

    @NotNull
    @Override
    public Optional<T> findById(@NotNull ID id) {
        return Optional.ofNullable(operations.findById(domainType, id));
    }

    @Override
    public boolean existsById(@NotNull ID id) {
        return findById(id).isPresent();
    }

    @NotNull
    @Override
    public Iterable<T> findAll() {
        return operations.findAll(domainType);
    }

    @NotNull
    @Override
    public Iterable<T> findAllById(@NotNull Iterable<ID> ids) {
        throw new NotImplementedException("구현 중");
    }

    @Override
    public long count() {
        return operations.count(domainType).get().value().longValue();
    }

    @Override
    public void deleteById(@NotNull ID id) {
        findById(id).ifPresent(this::delete);
    }

    @Override
    public void delete(@NotNull T entity) {
        operations.delete(entity);
    }

    @Override
    public void deleteAll(@NotNull Iterable<? extends T> entities) {
        operations.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        operations.delete(domainType).get().value();
    }

    @NotNull
    @Override
    public <S extends T> Optional<S> findOne(@NotNull Example<S> example) {
        throw new NotImplementedException("구현 중");
    }

    @NotNull
    @Override
    public <S extends T> Iterable<S> findAll(@NotNull Example<S> example) {
        throw new NotImplementedException("구현 중");
    }

    @NotNull
    @Override
    public <S extends T> Iterable<S> findAll(@NotNull Example<S> example, @NotNull Sort sort) {
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
    public void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata) {

    }

    @Override
    public Optional<T> findOne(@Nullable WhereAndOr<T> predicate) {
        return Optional.empty();
    }

    @Override
    public List<T> findAll(@Nullable WhereAndOr<T> predicate) {
        return null;
    }

    @Override
    public Page<T> findAll(@Nullable WhereAndOr<T> predicate, Pageable pageable) {
        return null;
    }

    @Override
    public List<T> findAll(@Nullable WhereAndOr<T> predicate, Sort sort) {
        return null;
    }

    @Override
    public long count(@Nullable WhereAndOr<T> predicate) {
        return 0;
    }
}
