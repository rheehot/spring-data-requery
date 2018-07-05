package org.springframework.data.requery.domain.sample

import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import io.requery.Table
import org.springframework.data.requery.domain.AbstractPersistable
import org.springframework.data.requery.domain.ToStringBuilder
import java.util.*

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

    abstract var firstname: String
    abstract var lastname: String

    abstract var email: String


    override fun hashCode(): Int {
        return Objects.hash(firstname, lastname, email)
    }

    // NOTE: buildStringHelder 도 메소드이므로 @Transient 를 꼭 지정해줘야 한다.
    @io.requery.Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("firstname", firstname)
            .add("lastname", lastname)
            .add("email", email)
    }
}