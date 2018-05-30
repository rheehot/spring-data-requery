package com.coupang.springframework.data.requery.repository.support

import com.coupang.springframework.data.requery.repository.RequeryContext
import io.requery.sql.EntityDataStore

/**
 * DefaultRequeryContext
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
open class DefaultRequeryContext(private val dataStore: EntityDataStore<*>): RequeryContext {

    override fun getEntityDataStoreByManagedType(manaedType: Class<*>): EntityDataStore<*> {
        return dataStore
    }
}