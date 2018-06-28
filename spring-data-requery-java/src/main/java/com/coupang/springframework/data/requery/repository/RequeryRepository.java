package com.coupang.springframework.data.requery.repository;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

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

    <S extends T> S upsert(S entity);

    <S extends T> List<S> upsertAll(Iterable<S> entities);

    void deleteInBatch(Iterable<T> entities);

    int deleteAllInBatch();

    T getOne(ID id);

    @Override
    <S extends T> List<S> findAll(Example<S> example);

    @Override
    <S extends T> List<S> findAll(Example<S> example, Sort sort);

}
