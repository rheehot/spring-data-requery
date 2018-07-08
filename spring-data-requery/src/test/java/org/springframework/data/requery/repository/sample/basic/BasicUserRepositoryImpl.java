package org.springframework.data.requery.repository.sample.basic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.requery.domain.basic.BasicUser;

/**
 * BasicUserRepositoryImpl
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@Slf4j
public class BasicUserRepositoryImpl implements BasicUserRepositoryCustom {

    public BasicUserRepositoryImpl() {}


    @Override
    public void findByOverridingMethod() {
        log.debug("A queryMethod overridng a finder was invoked!");
    }

    @Override
    public void someCustomMethod(BasicUser user) {
        log.debug("Some custom queryMethod was invoked!");
    }
}
