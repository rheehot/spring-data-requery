package com.coupang.springframework.data.requery.java.repository.custom;

import com.coupang.springframework.data.requery.java.domain.basic.AbstractBasicUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * com.coupang.springframework.data.requery.java.repository.custom.UserCustomExtendedRepository
 *
 * @author debop
 * @since 18. 6. 9
 */
public interface UserCustomExtendedRepository extends CustomGenericRepository<AbstractBasicUser, Long> {

    @Transactional(readOnly = false, timeout = 10)
    List<AbstractBasicUser> findAll();


    @Transactional(readOnly = false, timeout = 10)
    Optional<AbstractBasicUser> findById(Long id);

}
