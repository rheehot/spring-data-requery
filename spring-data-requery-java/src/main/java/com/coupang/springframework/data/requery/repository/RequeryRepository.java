package com.coupang.springframework.data.requery.repository;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * RequeryRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@NoRepositoryBean
public interface RequeryRepository<T, ID> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {

    @Autowired
    RequeryOperations getOperations();

    <S extends T> S upsert(S entity);

    <S extends T> Iterable<S> upsertAll(Iterable<S> entities);

    <S extends T> void deleteInBatch(Iterable<S> entities);

    int deleteAllInBatch();

    T getOne(ID id);
}
