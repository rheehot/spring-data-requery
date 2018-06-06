package com.coupang.springframework.data.requery.provider;

import org.jetbrains.annotations.Nullable;

/**
 * com.coupang.springframework.data.requery.provider.ProxyIdAccessor
 *
 * @author debop
 * @since 18. 6. 7
 */
public interface ProxyIdAccessor {

    boolean shouldUseAccessorFor(Object entity);

    @Nullable
    Object getIdentifierFrom(Object entity);
}
