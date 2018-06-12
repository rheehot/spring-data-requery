package com.coupang.springframework.data.requery.java.repository.sample.basic;

import com.coupang.springframework.data.requery.java.domain.basic.BasicUser;

/**
 * BasicUserRepositoryCustom
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
public interface BasicUserRepositoryCustom {

    void findByOverridingMethod();

    void someCustomMethod(BasicUser user);
}
