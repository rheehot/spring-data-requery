package org.springframework.data.requery.kotlin.repository.sample

import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.data.requery.kotlin.repository.RequeryRepository

/**
 * UserRepository
 *
 * @author debop@coupang.com
 */
@SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
interface UserRepository: RequeryRepository<SecurityProperties.User, Int>, UserRepositoryCustom {

    @JvmDefault
    fun findByLastname(lastname: String): List<SecurityProperties.User>

}