package org.springframework.data.requery.repository.sample

import mu.KotlinLogging
import org.springframework.data.requery.domain.sample.User

/**
 * org.springframework.data.requery.repository.sample.UserRepositoryImpl
 *
 * @author debop
 */
class UserRepositoryImpl: UserRepositoryCustom {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    override fun findByOverrridingMethod() {
        log.debug { "A method overriding a finder was invoked!" }
    }

    override fun someCustomMethod(user: User) {
        log.debug { "Some custom method was invoked! user=$user" }
    }
}