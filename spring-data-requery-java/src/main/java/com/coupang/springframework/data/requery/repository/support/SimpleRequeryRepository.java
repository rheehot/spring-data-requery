package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.repository.RequeryRepository;
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

    public SimpleRequeryRepository(RequeryOperations operations, Class<T> domainType) {
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

    @Override
    public Iterable<T> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends T> S save(S entity) {
        return operations.upsert(entity);
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        return operations.upsertAll(entities);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id) != null;
    }

    @Override
    public Iterable<T> findAll() {
        return operations.findAll(domainType);
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        return null;
    }

    @Override
    public long count() {
        return operations.count(domainType).get().value().longValue();
    }

    @Override
    public void deleteById(ID id) {
        findById(id).ifPresent(this::delete);
    }

    @Override
    public void delete(T entity) {
        operations.delete(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        operations.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        operations.delete(domainType).get().value();
    }

    @Override
    public <S extends T> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends T> Iterable<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends T> Iterable<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        return false;
    }
}
