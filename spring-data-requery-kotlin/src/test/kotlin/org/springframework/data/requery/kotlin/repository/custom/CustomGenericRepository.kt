package org.springframework.data.requery.kotlin.repository.custom

import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.requery.kotlin.repository.RequeryRepository

@NoRepositoryBean
interface CustomGenericRepository<E: Any, ID: Any>: RequeryRepository<E, ID> {

    fun customMethod(id: ID): E
}