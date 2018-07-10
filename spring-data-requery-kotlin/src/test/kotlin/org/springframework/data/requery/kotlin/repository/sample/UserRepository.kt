package org.springframework.data.requery.kotlin.repository.sample

import org.springframework.data.requery.kotlin.domain.sample.User
import org.springframework.data.requery.kotlin.repository.RequeryRepository

/**
 * UserRepository
 *
 * @author debop@coupang.com
 */
@SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
interface UserRepository: RequeryRepository<User, Int>, UserRepositoryCustom {

    @JvmDefault
    fun findByLastname(lastname: String): List<User>

}