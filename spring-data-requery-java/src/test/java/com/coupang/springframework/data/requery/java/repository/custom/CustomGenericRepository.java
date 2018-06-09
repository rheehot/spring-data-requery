package com.coupang.springframework.data.requery.java.repository.custom;

import com.coupang.springframework.data.requery.repository.RequeryRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Extension of {@link CrudRepository} to be added on a custom repository base class. This tests the facility to
 * implement custom base class functionality for all repository instances derived from this interface and implementation
 * base class.
 *
 * @author debop
 * @since 18. 6. 9
 */
public interface CustomGenericRepository<T, ID> extends RequeryRepository<T, ID> {

    /**
     * Custom sample method.
     */
    T customMethod(ID id);
}
