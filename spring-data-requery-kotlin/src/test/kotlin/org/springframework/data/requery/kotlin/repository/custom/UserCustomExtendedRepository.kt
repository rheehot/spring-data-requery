package org.springframework.data.requery.kotlin.repository.custom

import org.springframework.data.requery.kotlin.domain.sample.User
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface UserCustomExtendedRepository: CustomGenericRepository<User, Int> {

    @Transactional(readOnly = false, timeout = 10)
    override fun findAll(): List<User>

    @Transactional(readOnly = false, timeout = 10)
    override fun findById(id: Int): Optional<User>
}