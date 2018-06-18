package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.utils.PagingUtils;
import io.requery.query.*;
import io.requery.query.element.QueryElement;
import io.requery.sql.EntityDataStore;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.*;
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

    @Getter
    private final RequeryOperations operations;
    private final RequeryEntityInformation<T, ID> entityInformation;
    private final Class<T> domainClass;

    private CrudMethodMetadata crudMethodMetadata;

    public SimpleRequeryRepository(@NotNull RequeryEntityInformation<T, ID> entityInformation, @NotNull RequeryOperations operations) {
        log.debug("Create SimpleRequeryRepository. domainClass={}", entityInformation.getJavaType());

        this.entityInformation = entityInformation;
        this.domainClass = entityInformation.getJavaType();
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
    public <S extends T> Iterable<S> upsertAll(Iterable<S> entities) {
        return operations.upsertAll(entities);
    }

    @Transactional
    @Override
    public <S extends T> void deleteInBatch(Iterable<S> entities) {
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
    public Iterable<T> findAll(Sort sort) {
        if (sort != null && sort.isSorted()) {
            OrderingExpression<?>[] orderingExprs = PagingUtils.toRequeryOrderExpression(domainClass, sort);

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
    public Page<T> findAll(Pageable pageable) {
        if (pageable != null && pageable.isPaged()) {

            Return<Result<T>> query = PagingUtils.applyPageable(domainClass,
                                                                (QueryElement<Result<T>>) operations.select(domainClass),
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
    public <S extends T> Iterable<S> saveAll(@NotNull Iterable<S> entities) {
        return operations.upsertAll(entities);
    }

    @NotNull
    @Override
    public Optional<T> findById(@NotNull ID id) {
        return Optional.ofNullable(operations.findById(domainClass, id));
    }

    @Override
    public boolean existsById(@NotNull ID id) {
        return findById(id).isPresent();
    }

    @NotNull
    @Override
    public Iterable<T> findAll() {
        return operations.findAll(domainClass);
    }

    @NotNull
    @Override
    public Iterable<T> findAllById(@NotNull Iterable<ID> ids) {
        throw new NotImplementedException("구현 중");
    }

    @Override
    public long count() {
        return operations.count(domainClass).get().value().longValue();
    }

    @Transactional
    @Override
    public void deleteById(@NotNull ID id) {
        // TODO: Entity의 @Key 에 해당하는 Field 의 이름과 수형을 알아야 한다.
        //
        NamedExpression idExpr = NamedExpression.of("id", Long.class);
        operations.delete(domainClass)
            .where(idExpr.eq(id))
            .get();
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
        operations.delete(domainClass).get().value();
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
