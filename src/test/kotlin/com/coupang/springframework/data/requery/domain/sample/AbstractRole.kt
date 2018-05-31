package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.Entity
import io.requery.Generated
import io.requery.Key

/**
 * AbstractRole
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity
abstract class AbstractRole: AbstractPersistable<Int>() {

    @get:Key
    @get:Generated
    abstract override val id: Int?

    abstract var name: String?

}