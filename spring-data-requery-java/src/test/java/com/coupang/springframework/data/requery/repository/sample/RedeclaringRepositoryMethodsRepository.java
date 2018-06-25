package com.coupang.springframework.data.requery.repository.sample;

import com.coupang.springframework.data.requery.annotation.Query;
import com.coupang.springframework.data.requery.domain.sample.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * RedeclaringRepositoryMethodsRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
public interface RedeclaringRepositoryMethodsRepository extends CrudRepository<User, Long> {

    @Query("SELECT * FROM SD_User u where u.id = -1")
    List<User> findAll();

    @Query("SELECT * FROM SD_User u where u.firstname = 'Oliver'")
    Page<User> findAll(Pageable pageable);
}
