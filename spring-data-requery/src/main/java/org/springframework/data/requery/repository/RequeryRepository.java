package org.springframework.data.requery.repository;

import io.requery.meta.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.requery.core.RequeryOperations;

import java.util.List;

/**
 * RequeryRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@NoRepositoryBean
public interface RequeryRepository<T, ID>
    extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T>, RequeryWhereExecutor<T> {

    @Autowired
    RequeryOperations getOperations();

    @Override
    List<T> findAll();

    @Override
    List<T> findAll(Sort sort);

    @Override
    List<T> findAllById(Iterable<ID> ids);

    @Override
    <S extends T> List<S> saveAll(Iterable<S> entities);

    <S extends T> S insert(S entity);

    <S extends T, K> K insert(S entity, Class<K> keyClass);

    <S extends T> List<S> insert(Iterable<S> entities);

    <S extends T, K> List<K> insert(Iterable<S> entities, Class<K> keyClass);

    <S extends T> S upsert(S entity);

    <S extends T> List<S> upsertAll(Iterable<S> entities);

    <S extends T> S refresh(S entity);

    @SuppressWarnings("unchecked")
    <S extends T> List<S> refresh(Iterable<S> entities, Attribute<S, ?>... attributes);

    <S extends T> S refreshAll(S entity);

    void deleteInBatch(Iterable<T> entities);

    int deleteAllInBatch();

    T getOne(ID id);

    @Override
    <S extends T> List<S> findAll(Example<S> example);

    @Override
    <S extends T> List<S> findAll(Example<S> example, Sort sort);

}
