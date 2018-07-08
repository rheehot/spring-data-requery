package org.springframework.data.requery.repository.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.requery.domain.sample.User;

/**
 * UserRepositoryImpl
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Slf4j
public class UserRepositoryImpl implements UserRepositoryCustom {

    @Override
    public void findByOverrridingMethod() {
        log.debug("A method overriding a finder was invoked!");
    }

    @Override
    public void someCustomMethod(User user) {
        log.debug("Some custom method was invoked! user={}", user);
    }
}
