package org.springframework.data.requery.kotlin.repository.sample

import io.requery.query.Result
import io.requery.query.Return
import org.springframework.data.requery.kotlin.domain.sample.RoleEntity
import org.springframework.data.requery.kotlin.repository.RequeryRepository
import java.util.*

/**
 * RoleRepository
 *
 * @author debop@coupang.com
 */
interface RoleRepository: RequeryRepository<RoleEntity, Int> {

    override fun findAll(): List<RoleEntity>

    override fun findById(id: Int): Optional<RoleEntity>

    override fun findOne(filter: Return<out Result<RoleEntity>>): Optional<RoleEntity>

    fun countByName(name: String): Long
}