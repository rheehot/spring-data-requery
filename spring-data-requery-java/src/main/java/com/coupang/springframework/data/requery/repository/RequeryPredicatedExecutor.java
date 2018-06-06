package com.coupang.springframework.data.requery.repository;

import io.requery.query.WhereAndOr;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * com.coupang.springframework.data.requery.repository.RequeryPredicatedExecutor
 *
 * @author debop
 * @since 18. 6. 6
 */
public interface RequeryPredicatedExecutor<T> {

    Optional<T> findOne(@Nullable WhereAndOr<T> predicate);

    List<T> findAll(@Nullable WhereAndOr<T> predicate);

    Page<T> findAll(@Nullable WhereAndOr<T> predicate, Pageable pageable);

    List<T> findAll(@Nullable WhereAndOr<T> predicate, Sort sort);

    long count(@Nullable WhereAndOr<T> predicate);
}
