package org.springframework.data.requery.repository.sample

import org.springframework.data.requery.domain.sample.User
import org.springframework.data.requery.repository.RequeryRepository

/**
 * UserRepository
 *
 * @author debop@coupang.com
 */
interface UserRepository: RequeryRepository<User, Int> {

    @JvmDefault
    fun findByLastname(lastname: String): List<User>

}