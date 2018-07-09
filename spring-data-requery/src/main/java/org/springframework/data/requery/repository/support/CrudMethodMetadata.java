package org.springframework.data.requery.repository.support;

import java.lang.reflect.Method;

/**
 * org.springframework.data.requery.repository.support.CrudMethodMetadata
 *
 * @author debop
 * @since 18. 6. 6
 */
public interface CrudMethodMetadata {

    Method getMethod();

}
