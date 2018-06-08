package com.coupang.springframework.data.requery.repository.query;

import io.requery.query.NamedExpression;
import io.requery.query.Result;
import io.requery.query.Selection;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * QueryElementUtils
 *
 * @author debop@coupang.com
 * @since 18. 6. 8
 */
@Slf4j
@UtilityClass
public class QueryElementUtils {

    public static <E> Selection<Result<?>> addPaging(Class<E> domainClass,
                                                     Selection<Result<?>> selection,
                                                     Pageable pageable) {
        if (pageable.isUnpaged()) {
            return selection;
        }
        selection = (Selection<Result<?>>) selection
            .limit(pageable.getPageSize())
            .offset((int) pageable.getOffset());

        Sort sort = pageable.getSort();
        selection = addSort(domainClass, selection, sort);

        return selection;
    }

    public static <E> Selection<Result<?>> addSort(Class<E> domainClass,
                                                   Selection<Result<?>> selection,
                                                   Sort sort) {
        for (Sort.Order order : sort) {
            String propertyName = order.getProperty();
            Sort.Direction dir = order.getDirection();
            NamedExpression<E> expr = NamedExpression.of(propertyName, domainClass);

            selection = (Selection<Result<?>>) selection.orderBy(dir.isAscending() ? expr.asc() : expr.desc());
        }

        return selection;
    }


}
