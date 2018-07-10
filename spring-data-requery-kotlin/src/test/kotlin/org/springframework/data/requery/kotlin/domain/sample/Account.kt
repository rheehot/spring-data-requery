package org.springframework.data.requery.kotlin.domain.sample

import io.requery.Column
import io.requery.Entity
import io.requery.Key
import org.springframework.data.requery.kotlin.domain.PersistableObject

/**
 * org.springframework.data.requery.kotlin.domain.sample.Account
 *
 * @author debop
 */
@Entity(name = "Accounts")
interface Account: PersistableObject {

    @get:Key
    val id: Long

    @get:Column(name = "account_name")
    var name: String?
}