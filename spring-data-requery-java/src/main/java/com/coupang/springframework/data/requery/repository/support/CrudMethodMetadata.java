package com.coupang.springframework.data.requery.repository.support;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * com.coupang.springframework.data.requery.repository.support.CrudMethodMetadata
 *
 * @author debop
 * @since 18. 6. 6
 */
public interface CrudMethodMetadata {

    Map<String, Object> getQueryHints();

    Method getMethod();
}
