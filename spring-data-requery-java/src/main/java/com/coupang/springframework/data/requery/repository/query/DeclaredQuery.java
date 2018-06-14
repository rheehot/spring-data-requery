package com.coupang.springframework.data.requery.repository.query;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * A wrapper for a String representation of a query offering information about the query.
 *
 * @author debop
 * @since 18. 6. 14
 */
public interface DeclaredQuery {

    /**
     * Creates a {@literal DeclaredQuery} from a query {@literal String}.
     *
     * @param query might be {@literal null} or empty.
     * @return a {@literal DeclaredQuery} instance even for a {@literal null} or empty argument.
     */
    static DeclaredQuery of(@Nullable String query) {
        return StringUtils.isEmpty(query) ? EmptyDeclaredQuery.EMPTY_QUERY : new StringQuery(query);
    }

    /**
     * @return whether the underlying query has at least one named parameter.
     */
    boolean hasNamedParameter();

    /**
     * Returns the query string.
     */
    String getQueryString();

    /**
     * Returns the main alias used in the query.
     *
     * @return the alias
     */
    @Nullable
    String getAlias();

    /**
     * Returns whether the query is using a constructor expression.
     *
     * @since 1.10
     */
    boolean hasConstructorExpression();

    /**
     * Returns whether the query uses the default projection, i.e. returns the main alias defined for the query.
     */
    boolean isDefaultProjection();

    /**
     * Returns the {@link StringQuery.ParameterBinding}s registered.
     */
    List<StringQuery.ParameterBinding> getParameterBindings();

    /**
     * Creates a new {@literal DeclaredQuery} representing a count query, i.e. a query returning the number of rows to be
     * expected from the original query, either derived from the query wrapped by this instance or from the information
     * passed as arguments.
     *
     * @param countQuery           an optional query string to be used if present.
     * @param countQueryProjection an optional return type for the query.
     * @return a new {@literal DeclaredQuery} instance.
     */
    DeclaredQuery deriveCountQuery(@Nullable String countQuery, @Nullable String countQueryProjection);

    /**
     * @return whether paging is implemented in the query itself, e.g. using SpEL expressions.
     * @since 2.0.6
     */
    default boolean usesPaging() {
        return false;
    }

    /**
     * Returns whether the query uses JDBC style parameters, i.e. parameters denoted by a simple ? without any index or
     * name.
     *
     * @return Whether the query uses JDBC style parameters.
     * @since 2.0.6
     */
    boolean usesJdbcStyleParameters();
}
