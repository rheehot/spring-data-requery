package org.springframework.data.requery.repository.sample

import io.requery.query.Result
import io.requery.query.Return
import org.springframework.data.requery.kotlin.domain.sample.Role
import org.springframework.data.requery.repository.RequeryRepository
import java.util.*

/**
 * RoleRepository
 *
 * @author debop@coupang.com
 */
interface RoleRepository: RequeryRepository<Role, Int> {

    override fun findAll(): List<Role>

    override fun findById(id: Int): Optional<Role>

    override fun findOne(filter: Return<out Result<Role>>): Optional<Role>

    fun countByName(name: String): Long
}