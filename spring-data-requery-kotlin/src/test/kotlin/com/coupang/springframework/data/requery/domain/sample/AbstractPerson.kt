package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.Column
import io.requery.Entity
import io.requery.Generated
import io.requery.Key

/**
 * AbstractPerson
 *
 * @author debop@coupang.com
 * @since 18. 5. 30
 */
@Entity(copyable = true, cacheable = true)
abstract class AbstractPerson: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract val id: Long?

    @get:Column(name = "person_name", length = 48)
    abstract var name: String?

}