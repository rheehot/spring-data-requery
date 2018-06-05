package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.Column
import io.requery.Entity
import io.requery.Generated
import io.requery.Key

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractAccount
 * @author debop
 * @since 18. 5. 30
 */
@Entity
abstract class AbstractAccount: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract val id: Long?


    @get:Column(name = "account_name", length = 48)
    abstract var name: String?
}