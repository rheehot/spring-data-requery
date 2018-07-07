package org.springframework.data.requery.domain.sample

import io.requery.*
import org.springframework.data.requery.domain.AbstractPersistable
import org.springframework.data.requery.domain.ToStringBuilder
import java.util.*

/**
 * org.springframework.data.requery.domain.sample.AbstractVersionedUser
 *
 * @author debop
 */
@Entity
abstract class AbstractVersionedUser: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long

    @get:Column(name = "user_name")
    abstract var name: String

    @get:Column(name = "user_email")
    abstract var email: String

    @get:Column(name = "user_birthday")
    abstract var birthday: Date

    //NOTE: @Version 을 지정하기 위해서는 @JvmField를 지정하고, final로 지정해야 합니다.
    @Version
    @JvmField
    final var version: Long = 0

    override fun hashCode(): Int = Objects.hash(name, email, birthday)

    @io.requery.Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("name", name)
            .add("email", email)
            .add("birthday", birthday)
    }
}