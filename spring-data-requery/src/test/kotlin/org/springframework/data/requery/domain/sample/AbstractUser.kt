package org.springframework.data.requery.domain.sample

import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import io.requery.Table
import org.springframework.data.requery.domain.AbstractPersistable

/**
 * AbstractUser
 *
 * @author debop@coupang.com
 */
@Entity
@Table(name = "SD_User")
abstract class AbstractUser: AbstractPersistable<Int>() {

    @get:Key
    @get:Generated
    abstract override val id: Int

    abstract var firstName: String
    abstract var lastname: String

    abstract var email: String
}