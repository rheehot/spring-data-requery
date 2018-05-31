package com.coupang.springframework.data.requery.domain.functional

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.Entity
import io.requery.Generated
import io.requery.Key

/**
 * AbstractFuncPerson
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity
abstract class AbstractFuncPerson: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

}