package com.coupang.springframework.data.requery.repository.sample;

import com.coupang.springframework.data.requery.annotation.Modifying;
import com.coupang.springframework.data.requery.annotation.Query;
import com.coupang.springframework.data.requery.domain.sample.AuditableUser;
import com.coupang.springframework.data.requery.repository.RequeryRepository;

import java.util.List;

/**
 * AuditableUserRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
public interface AuditableUserRepository extends RequeryRepository<AuditableUser, Long> {

    List<AuditableUser> findByFirstname(final String firstname);

    // TODO : Spring EL 을 이용한 값 지정하기
    // a.lastModifiedDate = :#{T(org.springframework.data.jpa.util.FixedDate).INSTANCE.getDate()}

    @Modifying
    @Query("update AuditableUser a set a.firstname = upper(a.firstname), a.lastModifiedDate = Date()")
    void udpateAllNamesToUpperCase();
}
