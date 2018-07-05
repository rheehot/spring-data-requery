package org.springframework.data.requery.domain.sample

import io.requery.Column
import io.requery.Entity
import io.requery.Key
import org.springframework.data.requery.domain.AbstractPersistable

/**
 * AbstractAccount
 *
 * @author debop@coupang.com
 */
@Entity(name = "Accounts")
abstract class AbstractAccount: AbstractPersistable<Long>() {

    @get:Key
    override var id: Long = 0

    @get:Column(name = "account_name")
    abstract var name: String

}