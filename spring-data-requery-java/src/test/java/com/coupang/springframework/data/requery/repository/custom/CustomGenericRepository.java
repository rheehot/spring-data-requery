package com.coupang.springframework.data.requery.repository.custom;

import com.coupang.springframework.data.requery.repository.RequeryRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Extension of {@link CrudRepository} to be added on a custom repository base class. This tests the facility to
 * implement custom base class functionality for all repository instances derived from this interface and implementation
 * base class.
 *
 * @author debop
 * @since 18. 6. 9
 */
@NoRepositoryBean
public interface CustomGenericRepository<T, ID> extends RequeryRepository<T, ID> {

    /**
     * Custom sample queryMethod.
     */
    T customMethod(ID id);
}
