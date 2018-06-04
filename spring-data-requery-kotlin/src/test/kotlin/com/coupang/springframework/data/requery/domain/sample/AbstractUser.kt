package com.coupang.springframework.data.requery.domain.sample

import com.coupang.kotlinx.data.requery.conveters.ByteArrayBlobConverter
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*
import java.util.*

/**
 * AbstractUser
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity
@Table(name = "SD_Users")
abstract class AbstractUser: AbstractPersistable<Int>() {

    @get:Key
    @get:Generated
    @get:Column(name = "user_id")
    abstract override val id: Int?

    abstract var firstname: String?
    abstract var lastname: String?
    abstract var age: Int?
    abstract var active: Boolean?

    abstract var createdAt: Date?

    @get:Column(nullable = false, unique = true)
    abstract var emailAddress: String

    @get:ManyToMany(mappedBy = "colleague")
    @get:JunctionTable(name = "User_Colleagues")
    abstract val colleagues: MutableSet<AbstractUser>

    @get:ManyToMany
    abstract val roles: MutableSet<AbstractRole>

    @get:ManyToOne
    @get:ForeignKey(referencedColumn = "manager_id")
    abstract var manager: AbstractUser?

    @get:Embedded
    abstract val address: AbstractAddress

    @get:Convert(ByteArrayBlobConverter::class)
    abstract var byinaryData: ByteArray?

    // NOTE: JPA @ElementCollection 처럼 단순 수형에 대해서는 이렇게 못한다. @OneToMany 처럼 해야 한다.
    abstract val attributes: MutableSet<String>

    @get:Column(name = "birthday")
    abstract var dateOfBirth: Date?


    fun addColleague(colleague: AbstractUser) {
        if(this === colleague) {
            return
        }

        this.colleagues.add(colleague)
        colleague.colleagues.add(this)
    }


    fun addRole(role: AbstractRole) {
        this.roles.add(role)
    }

    fun removeRole(role: AbstractRole) {
        this.roles.remove(role)
    }


    @PreInsert
    fun onPreInsert() {
        createdAt = Date()
    }
}