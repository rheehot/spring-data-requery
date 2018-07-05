package org.springframework.data.requery.domain.sample;

import io.requery.query.Condition;
import io.requery.query.Expression;
import io.requery.query.NamedExpression;
import io.requery.query.Result;
import io.requery.query.element.QueryElement;
import lombok.experimental.UtilityClass;
import org.springframework.data.requery.core.RequeryOperations;

/**
 * Collection of {@link QueryElement}s for a {@link User}
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@UtilityClass
@SuppressWarnings("unchecked")
public class UserQueryElements {

    public static QueryElement<? extends Result<User>> userHasFirstname(RequeryOperations operations, final String firstname) {
        return simplePropertyQueryElement(operations, "firstname", firstname);
    }

    public static QueryElement<? extends Result<User>> userHasLastname(RequeryOperations operations, final String lastname) {
        return simplePropertyQueryElement(operations, "lastname", lastname);
    }

    public static QueryElement<? extends Result<User>> userHasFirstnameLike(RequeryOperations operations,
                                                                            final String expression) {
        Condition<?, ?> filter = NamedExpression.ofString("firstname").like("%" + expression + "%");

        return (QueryElement<? extends Result<User>>) operations
            .select(User.class)
            .where(filter);
    }

    public static QueryElement<? extends Result<User>> userHasLastnameLikeWithSort(RequeryOperations operations,
                                                                                   final String expression) {
        Condition<?, ?> expr = NamedExpression.ofString("lastname").like("%" + expression + "%");
        Expression<String> firstNameExpr = NamedExpression.ofString("firstname");

        return (QueryElement<? extends Result<User>>) operations
            .select(User.class)
            .where(expr)
            .orderBy(firstNameExpr);
    }

    @SuppressWarnings("unchecked")
    private QueryElement<? extends Result<User>> simplePropertyQueryElement(RequeryOperations operations,
                                                                            String propertyName,
                                                                            final String propertyValue) {
        Condition<?, ?> filter = NamedExpression.ofString(propertyName).eq(propertyValue);

        return (QueryElement<? extends Result<User>>) operations
            .select(User.class)
            .where(filter);
    }
}
