package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.repository.sample.AuditableUserRepository
import org.springframework.data.domain.AuditorAware
import java.util.*

/**
 * com.coupang.springframework.data.requery.domain.sample.AuditorAwareSub
 * @author debop
 * @since 18. 5. 30
 */
class AuditorAwareSub(private val repository: AuditableUserRepository): AuditorAware<AuditableUser> {

    private var auditor: AuditableUser? = null

    fun setAuditor(auditor: AuditableUser) {
        this.auditor = auditor
    }

    override fun getCurrentAuditor(): Optional<AuditableUser> = Optional.ofNullable(auditor)

}