package com.coupang.springframework.data.requery.java.repository.sample.basic;

import com.coupang.springframework.data.requery.java.domain.basic.BasicUser;
import com.coupang.springframework.data.requery.repository.RequeryRepository;

/**
 * com.coupang.springframework.data.requery.java.repository.sample.basic.BasicUserRepository
 *
 * @author debop
 * @since 18. 6. 9
 */
public interface BasicUserRepository extends RequeryRepository<BasicUser, Long> {
}
