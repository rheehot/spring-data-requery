package com.coupang.springframework.data.requery.domain.functional

import com.coupang.kotlinx.core.hashOf
import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*
import io.requery.converter.LocalDateConverter
import java.net.URL
import java.time.LocalDate
import java.util.*

@Entity
abstract class AbstractFuncPerson: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    @get:Column(name = "personId")
    abstract override val id: Long?


    @get:Index(value = ["idx_func_person_name_email"])
    abstract var name: String

    @get:Index(value = ["idx_func_person_name_email"])
    abstract var email: String

    @get:Convert(LocalDateConverter::class)
    abstract var birthday: LocalDate

    @get:Column(value = "'empty'")
    abstract var description: String?

    @get:Nullable
    abstract var age: Int?

    @get:ForeignKey
    @get:OneToOne(mappedBy = "person", cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    abstract var address: AbstractFuncAddress?

    @get:OneToMany(mappedBy = "owner", cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    abstract val phoneNumbers: MutableSet<AbstractFuncPhone>

    @get:OneToMany
    abstract val phoneNumberList: MutableList<AbstractFuncPhone>

    @get:ManyToMany(mappedBy = "members")
    abstract val groups: MutableSet<AbstractFuncGroup>

    @get:ManyToMany(mappedBy = "owners")
    abstract val ownedGroups: MutableSet<AbstractFuncGroup>

    @get:ManyToMany(mappedBy = "personId")
    @get:JunctionTable
    abstract val friends: MutableSet<AbstractFuncPerson>

    @get:Lazy
    @get:Nullable
    abstract var about: String?

    @get:Column(unique = true)
    abstract var uuid: UUID

    abstract var homepage: URL?

    abstract var picture: String?

    override fun hashCode(): Int {
        return hashOf(name, email)
    }

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("name", name)
            .add("email", email)
    }
}