package org.springframework.data.requery.repository.query;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.query.ParametersParameterAccessor;

/**
 * This class provides access to parameters of a user-defined queryMethod. It wraps ParametersParameterAccessor which catches
 * special parameters Sort and Pageable, and catches Arango-specific parameters e.g. AqlQueryOptions.
 *
 * @author debop@coupang.com
 * @since 18. 6. 8
 */
@Slf4j
public class RequeryParametersParameterAccessor extends ParametersParameterAccessor implements RequeryParameterAccessor {

    private final RequeryParameters parameters;

    public RequeryParametersParameterAccessor(@NotNull RequeryQueryMethod method, Object[] values) {
        this(method.getParameters(), values);
    }

    public RequeryParametersParameterAccessor(RequeryParameters parameters, Object[] values) {
        super(parameters, values);
        this.parameters = parameters;
    }

    @Override
    public RequeryParameters getParameters() {
        return parameters;
    }
}

