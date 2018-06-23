package com.coupang.springframework.data.requery.repository.sample.basic;

import com.coupang.springframework.data.requery.domain.basic.BasicUser;
import lombok.extern.slf4j.Slf4j;

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
