package com.coupang.springframework.data.requery.repository.sample.basic;

import com.coupang.springframework.data.requery.domain.basic.BasicUser;

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
