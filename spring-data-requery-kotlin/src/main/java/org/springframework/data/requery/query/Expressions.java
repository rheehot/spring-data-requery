package org.springframework.data.requery.query;

import io.requery.query.Expression;
import io.requery.query.FieldExpression;
import io.requery.query.LogicalCondition;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Expressions
 * <p>
 * NOTE: Java API 를 Kotlin 에서 호출 시에 ambiguous type error 가 발생할 때가 있다.
 * NOTE: 이 때에는 원하는 메소드만 Java Code 로 구현해서, Kotlin에서 호출하면 됩니다.
 *
 * @author debop@coupang.com
 */
public final class Expressions {

    private Expressions() {}

    @SuppressWarnings("unchecked")
    public static <V> LogicalCondition<? extends Expression<V>, Collection<V>> in(@NotNull FieldExpression<V> expression,
                                                                                  @NotNull Collection<V> values) {
        return expression.in(values);
    }

    @SuppressWarnings("unchecked")
    public static <V> LogicalCondition<? extends Expression<V>, Collection<V>> notIn(@NotNull FieldExpression<V> expression,
                                                                                     @NotNull Collection<V> values) {
        return expression.notIn(values);
    }
}
