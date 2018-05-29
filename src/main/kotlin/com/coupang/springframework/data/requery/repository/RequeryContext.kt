package com.coupang.springframework.data.requery.repository

import io.requery.sql.EntityDataStore

/**
 * RequeryContext
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
interface RequeryContext {

    fun getEntityDataStoreByManagedType(manaedType: Class<*>): EntityDataStore<*>
}