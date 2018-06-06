package com.coupang.springframework.data.requery.provider;

import org.jetbrains.annotations.Nullable;

/**
 * com.coupang.springframework.data.requery.provider.RequeryProxyIdAccessor
 *
 * @author debop
 * @since 18. 6. 7
 */
public class RequeryProxyIdAccessor implements ProxyIdAccessor {

    @Override
    public boolean shouldUseAccessorFor(Object entity) {
        return false;
    }

    @Override
    public @Nullable Object getIdentifierFrom(Object entity) {
        return null;
    }
}
