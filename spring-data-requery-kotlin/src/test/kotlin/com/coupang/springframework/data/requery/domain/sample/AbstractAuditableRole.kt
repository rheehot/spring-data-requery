package com.coupang.springframework.data.requery.domain.sample

import io.requery.Entity
import io.requery.Generated
import io.requery.Key

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractAuditableRole
 * @author debop
 * @since 18. 5. 30
 */
@Entity
abstract class AbstractAuditableRole: AbstractAuditable<AbstractAuditableUser, Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    abstract var name: String?
}