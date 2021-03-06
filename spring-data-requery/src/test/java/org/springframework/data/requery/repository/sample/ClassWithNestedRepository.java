package org.springframework.data.requery.repository.sample;

import org.springframework.data.requery.domain.basic.BasicUser;
import org.springframework.data.requery.repository.RequeryRepository;

/**
 * ClassWithNestedRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
public class ClassWithNestedRepository {

    public static interface NestedUserRepository extends RequeryRepository<BasicUser, Long> {}
}
