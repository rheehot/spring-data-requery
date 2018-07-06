package org.springframework.data.requery.repository.custom

import org.springframework.data.requery.repository.RequeryRepository

/**
 * org.springframework.data.requery.repository.custom.CustomGenericRepository
 *
 * @author debop
 */
interface CustomGenericRepository<E: Any, ID: Any>: RequeryRepository<E, ID> {

    fun customMethod(id: ID): E
}