package com.coupang.springframework.data.requery.repository.sample.basic;

import com.coupang.springframework.data.requery.domain.basic.BasicUser;
import com.coupang.springframework.data.requery.repository.RequeryContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * BasicUserRepositoryImpl
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@Slf4j
public class BasicUserRepositoryImpl implements BasicUserRepositoryCustom {

    @Autowired
    public BasicUserRepositoryImpl(RequeryContext context) {
        Assert.notNull(context, "RequeryContext must not be null");
    }


    @Override
    public void findByOverridingMethod() {
        log.debug("A queryMethod overridng a finder was invoked!");
    }

    @Override
    public void someCustomMethod(BasicUser user) {
        log.debug("Some custom queryMethod was invoked!");
    }
}
