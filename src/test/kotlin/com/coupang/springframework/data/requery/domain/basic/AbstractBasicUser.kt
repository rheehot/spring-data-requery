package com.coupang.springframework.data.requery.domain.basic

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.Entity
import io.requery.Generated
import io.requery.Key

/**
 * AbstractBasicUser
 *
 * @author debop@coupang.com
 * @since 18. 6. 1
 */
@Entity
abstract class AbstractBasicUser: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

}