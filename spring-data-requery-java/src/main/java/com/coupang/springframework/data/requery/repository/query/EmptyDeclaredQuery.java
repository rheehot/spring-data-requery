package com.coupang.springframework.data.requery.repository.query;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * com.coupang.springframework.data.requery.repository.query.EmptyDeclaredQuery
 *
 * @author debop
 * @since 18. 6. 14
 */
public class EmptyDeclaredQuery implements DeclaredQuery {

    /**
     * An implementation implementing the NULL-Object pattern for situations where there is no query.
     */
    static final DeclaredQuery EMPTY_QUERY = new EmptyDeclaredQuery();

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.query.DeclaredQuery#hasNamedParameter()
     */
    @Override
    public boolean hasNamedParameter() {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.query.DeclaredQuery#getQueryString()
     */
    @Override
    public String getQueryString() {
        return "";
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.query.DeclaredQuery#getAlias()
     */
    @Override
    public String getAlias() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.query.DeclaredQuery#hasConstructorExpression()
     */
    @Override
    public boolean hasConstructorExpression() {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.query.DeclaredQuery#isDefaultProjection()
     */
    @Override
    public boolean isDefaultProjection() {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.query.DeclaredQuery#getParameterBindings()
     */
    @Override
    public List<StringQuery.ParameterBinding> getParameterBindings() {
        return Collections.emptyList();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.query.DeclaredQuery#deriveCountQuery(java.lang.String, java.lang.String)
     */
    @Override
    public DeclaredQuery deriveCountQuery(@Nullable String countQuery, @Nullable String countQueryProjection) {

        Assert.hasText(countQuery, "CountQuery must not be empty!");

        return DeclaredQuery.of(countQuery);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.query.DeclaredQuery#usesJdbcStyleParameters()
     */
    @Override
    public boolean usesJdbcStyleParameters() {
        return false;
    }
}
