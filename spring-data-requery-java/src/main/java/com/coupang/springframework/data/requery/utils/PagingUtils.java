package com.coupang.springframework.data.requery.utils;

import io.requery.query.*;
import io.requery.query.element.QueryElement;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * PagingUtils
 *
 * @author debop@coupang.com
 * @since 18. 6. 18
 */
@UtilityClass
public class PagingUtils {

    public static <E> Return<Result<E>> applyPageable(Class<E> domainClass, QueryElement<Result<E>> baseQuery, Pageable pageable) {
        OrderingExpression<?>[] orderingExprs = toRequeryOrderExpression(domainClass, pageable.getSort());


        Limit<Result<E>> query = (orderingExprs.length > 0)
                                 ? baseQuery.orderBy(orderingExprs)
                                 : baseQuery;

        return
            query
                .limit(pageable.getPageSize())
                .offset((int) pageable.getOffset());
    }

    public static <E> OrderingExpression<?>[] toRequeryOrderExpression(Class<E> domainClass, Sort sort) {
        if (sort == null || sort.isUnsorted()) {
            return new OrderingExpression[0];
        }

        List<OrderingExpression<?>> orderingExprs = new ArrayList<>();

        for (Sort.Order order : sort) {
            String propertyName = order.getProperty();
            Field field = ReflectionUtils.findField(domainClass, propertyName);

            if (field != null) {
                NamedExpression<?> expr = NamedExpression.of(propertyName, field.getType());
                OrderingExpression<?> orderingExpr = (order.isAscending()) ? expr.asc() : expr.desc();

                orderingExprs.add(orderingExpr);
            }
        }

        return orderingExprs.toArray(new OrderingExpression<?>[0]);

    }
}
