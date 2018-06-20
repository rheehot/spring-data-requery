package com.coupang.springframework.data.requery.repository;

import io.requery.query.Condition;
import io.requery.query.Result;
import io.requery.query.Return;
import io.requery.query.WhereAndOr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * RequeryWhereExecutor
 *
 * @author debop@coupang.com
 * @since 18. 6. 20
 */
public interface RequeryWhereExecutor<T> {

    Optional<T> findOne(Return<? extends Result<T>> whereClause);

    List<T> findAll(Return<? extends Result<T>> whereClause);

    Page<T> findAll(WhereAndOr<? extends Result<T>> whereClause, Pageable pageable);

    List<T> findAll(Iterable<Condition<T, ?>> conditions, Sort sort);

    List<T> findAll(Iterable<Condition<T, ?>> conditions);

    int count(WhereAndOr<? extends Result<T>> whereClause);

    boolean exists(WhereAndOr<? extends Result<T>> whereClause);
}
