package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.repository.RequeryRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

/**
 * SimpleRequeryRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
public class SimpleRequeryRepository<T, ID> implements RequeryRepository<T, ID> {

    private final RequeryOperations operations;
    private final Class<T> domainType;

    public SimpleRequeryRepository(@NotNull RequeryOperations operations, @NotNull Class<T> domainType) {
        this.operations = operations;
        this.domainType = domainType;
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
}
