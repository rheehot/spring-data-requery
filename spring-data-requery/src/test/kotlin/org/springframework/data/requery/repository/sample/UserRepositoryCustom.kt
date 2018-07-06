package org.springframework.data.requery.repository.sample

import org.springframework.data.requery.domain.sample.User

/**
 * org.springframework.data.requery.repository.sample.UserRepositoryCustom
 *
 * @author debop
 */
interface UserRepositoryCustom {

    /**
     * Method actually triggering a finder but being overridden.
     */
    fun findByOverrridingMethod()

    /**
     * Some custom method to implement.
     */
    fun someCustomMethod(user: User)
}