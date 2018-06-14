package com.coupang.springframework.data.requery.repository.query;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for {@link String} based queries.
 *
 * @author debop
 * @since 18. 6. 14
 */
abstract class AbstractStringBasedRequeryQuery extends AbstractRequeryQuery {

    public AbstractStringBasedRequeryQuery(@NotNull RequeryQueryMethod method,
                                           @NotNull RequeryOperations operations) {
        super(method, operations);
    }
}
