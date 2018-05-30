package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.Column
import io.requery.Entity
import io.requery.Generated
import io.requery.Key

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractDummy
 * @author debop
 * @since 18. 5. 30
 */
@Entity
abstract class AbstractDummy @JvmOverloads constructor(aname: String? = null): AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    @get:Column(name = "dummy_name", length = 48)
    var name: String? = aname

    override fun toString(): String = "Dummy [id=$id, name=$name]"
}