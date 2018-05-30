package com.coupang.springframework.data.requery.repository.sample

import com.coupang.springframework.data.requery.domain.sample.AuditableUser
import com.coupang.springframework.data.requery.repository.RequeryRepository

/**
 * com.coupang.springframework.data.requery.repository.sample.AuditableUserRepository
 * @author debop
 * @since 18. 5. 30
 */
interface AuditableUserRepository: RequeryRepository<AuditableUser, Long> {

    fun findByFirstname(firstname: String): List<AuditableUser>
}