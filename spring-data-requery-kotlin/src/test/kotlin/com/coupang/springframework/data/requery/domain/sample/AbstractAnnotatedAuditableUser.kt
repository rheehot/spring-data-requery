package com.coupang.springframework.data.requery.domain.sample

import io.requery.Entity
import io.requery.Generated
import io.requery.Key

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractAnnotatedAuditableUser
 * @author debop
 * @since 18. 5. 30
 */
@Entity
abstract class AbstractAnnotatedAuditableUser: AbstractAnnotatedAuditable() {

    @get:Key
    @get:Generated
    abstract val id: Long

}