package org.springframework.data.requery.domain.sample

import io.requery.*
import org.springframework.data.requery.converters.ByteArrayToBlobConverter
import org.springframework.data.requery.domain.AbstractPersistable
import org.springframework.data.requery.domain.ToStringBuilder
import java.util.*

/**
 * AbstractUser
 *
 * @author debop@coupang.com
 */
@Entity(name = "User", copyable = true, stateless = true)
@Table(name = "SD_USER")
abstract class AbstractUser: AbstractPersistable<Int>() {

    @get:Key
    @get:Generated
    abstract override val id: Int

    abstract var firstname: String
    abstract var lastname: String
    abstract var age: Int
    abstract var boolean: Boolean

    @get:Column(nullable = false, unique = true)
    abstract var emailAddress: String

    // Self reference
    //    @get:JunctionTable(name = "User_Colleagues")
    //    @get:ManyToMany
    //    abstract val colleagues: Set<AbstractUser>

    //    @get:JunctionTable
    //    @get:ManyToMany
    //    abstract val roles: Set<AbstractRole>

    //    @get:ManyToOne
    //    abstract var manager: AbstractUser?

    //    // NOTE: Embedded 에 해당하는 놈은 abstract 를 빼야 한다.
    //    @get:Embedded
    //    var address: AbstractAddress = uninitialized()

    @get:Column
    @get:Convert(ByteArrayToBlobConverter::class)
    abstract var binaryData: ByteArray?

    @get:Column
    abstract var dateOfBirth: Date?


    override fun hashCode(): Int {
        return Objects.hash(firstname, lastname, emailAddress)
    }

    // NOTE: buildStringHelder 도 메소드이므로 @Transient 를 꼭 지정해줘야 한다.
    @io.requery.Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("firstname", firstname)
            .add("lastname", lastname)
            .add("emailAddress", emailAddress)
    }
}