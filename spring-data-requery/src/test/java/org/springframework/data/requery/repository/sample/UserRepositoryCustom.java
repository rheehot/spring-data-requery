package org.springframework.data.requery.repository.sample;

import org.springframework.data.requery.domain.sample.User;

/**
 * UserRepositoryCustom
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
public interface UserRepositoryCustom {

    /**
     * Method actually triggering a finder but being overridden.
     */
    void findByOverrridingMethod();

    /**
     * Some custom method to implement.
     */
    void someCustomMethod(User user);
}
