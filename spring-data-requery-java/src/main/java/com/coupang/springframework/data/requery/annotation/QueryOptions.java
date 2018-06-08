package com.coupang.springframework.data.requery.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * QueryOptions
 *
 * @author debop@coupang.com
 * @since 18. 6. 8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface QueryOptions {

    /**
     * @return Flag to determine whether the AQL query cache shall be used. If set to false, then any query cache lookup
     * will be skipped for the query. If set to true, it will lead to the query cache being checked for the
     * query if the query cache mode is either on or demand.
     */
    boolean cache() default false;

    /**
     * @return Indicates whether the number of documents in the result set should be returned in the "count" attribute
     * of the result. Calculating the "count" attribute might have a performance impact for some queries in the
     * future so this option is turned off by default, and "count" is only returned when requested.
     */
    boolean count() default false;

    /**
     * @return If set to true and the query contains a LIMIT clause, then the result will have an extra attribute with
     * the sub-attributes stats and fullCount, { ... , "extra": { "stats": { "fullCount": 123 } } }. The
     * fullCount attribute will contain the number of documents in the result before the last LIMIT in the query
     * was applied. It can be used to count the number of documents that match certain filter criteria, but only
     * return a subset of them, in one go. It is thus similar to MySQL's SQL_CALC_FOUND_ROWS hint. Note that
     * setting the option will disable a few LIMIT optimizations and may lead to more documents being processed,
     * and thus make queries run longer. Note that the fullCount attribute will only be present in the result if
     * the query has a LIMIT clause and the LIMIT clause is actually used in the query.
     */
    boolean fullCount() default false;
}
