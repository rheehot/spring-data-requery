package com.coupang.springframework.data.requery.domain.sample

import io.requery.*

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractAuditableUser
 * @author debop
 * @since 18. 5. 30
 */
@Entity
abstract class AbstractAuditableUser: AbstractAuditable<AbstractAuditableUser, Int>() {

    @get:Key
    @get:Generated
    abstract override val id: Int?

    abstract var firstname: String?

    @get:ManyToMany
    @get:JunctionTable
    abstract val roles: MutableSet<AbstractAuditableRole>


    fun addRole(role: AbstractAuditableRole) {
        this.roles += role
    }
}