package org.springframework.data.requery.repository.custom;

import org.springframework.data.requery.domain.basic.BasicUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserCustomExtendedRepository extends CustomGenericRepository<BasicUser, Long> {

    @Transactional(readOnly = false, timeout = 10)
    List<BasicUser> findAll();


    @Transactional(readOnly = false, timeout = 10)
    Optional<BasicUser> findById(Long id);

}
