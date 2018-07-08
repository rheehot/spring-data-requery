package org.springframework.data.requery.kotlin.domain.sample

import io.requery.Column
import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import org.springframework.data.requery.kotlin.domain.AbstractPersistable

/**
 * AbstractAccount
 *
 * @author debop@coupang.com
 */
@Entity(name = "Accounts")
abstract class AbstractAccount: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long

    @get:Column(name = "account_name")
    abstract var name: String

}