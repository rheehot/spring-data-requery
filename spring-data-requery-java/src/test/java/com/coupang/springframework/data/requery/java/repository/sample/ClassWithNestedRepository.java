package com.coupang.springframework.data.requery.java.repository.sample;

import com.coupang.springframework.data.requery.java.domain.basic.BasicUser;
import com.coupang.springframework.data.requery.repository.RequeryRepository;

/**
 * ClassWithNestedRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
public class ClassWithNestedRepository {

    public static interface NestedUserRepository extends RequeryRepository<BasicUser, Long> {}
}
