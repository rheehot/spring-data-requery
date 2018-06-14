package com.coupang.springframework.data.requery.java.repository.sample.basic;

import com.coupang.springframework.data.requery.annotation.Query;
import com.coupang.springframework.data.requery.java.domain.basic.BasicUser;
import com.coupang.springframework.data.requery.repository.RequeryRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * com.coupang.springframework.data.requery.java.repository.sample.basic.BasicUserRepository
 *
 * @author debop
 * @since 18. 6. 9
 */
public interface BasicUserRepository extends RequeryRepository<BasicUser, Long> {

    default List<BasicUser> findAllByName(String name) {
        return getOperations()
            .select(BasicUser.class)
            .where(BasicUser.NAME.eq(name))
            .get()
            .toList();
    }

    default BasicUser findByEmail(String email) {
        return getOperations()
            .select(BasicUser.class)
            .where(BasicUser.EMAIL.eq(email))
            .get()
            .firstOrNull();
    }

    @Query("select * from basic_user u where u.email = ?")
    @Transactional(readOnly = true)
    BasicUser findByAnnotatedQuery(String email);
}
