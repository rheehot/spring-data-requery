package com.coupang.springframework.data.requery.repository.query;

import org.springframework.data.repository.query.ParameterAccessor;

/**
 * RequeryParameterAccessor
 *
 * @author debop@coupang.com
 * @since 18. 6. 8
 */
public interface RequeryParameterAccessor extends ParameterAccessor {

    RequeryParameters getParameters();

}
