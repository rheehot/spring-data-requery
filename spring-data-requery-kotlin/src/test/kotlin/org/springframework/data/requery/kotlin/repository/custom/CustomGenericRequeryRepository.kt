package org.springframework.data.requery.kotlin.repository.custom

import org.springframework.data.requery.kotlin.core.RequeryOperations
import org.springframework.data.requery.kotlin.repository.support.RequeryEntityInformation
import org.springframework.data.requery.kotlin.repository.support.SimpleRequeryRepository

/**
 * org.springframework.data.requery.repository.custom.CustomGenericRequeryRepository
 *
 * @author debop
 */
class CustomGenericRequeryRepository<E: Any, ID: Any>(entityInformation: RequeryEntityInformation<E, ID>,
                                                      operations: RequeryOperations)
    : SimpleRequeryRepository<E, ID>(entityInformation, operations), CustomGenericRepository<E, ID> {

    override fun customMethod(id: ID): E {
        throw UnsupportedOperationException("Forced exception for testing purposes")
    }
}