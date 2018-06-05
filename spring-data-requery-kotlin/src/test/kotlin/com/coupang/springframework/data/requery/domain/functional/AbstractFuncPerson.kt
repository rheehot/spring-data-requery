package com.coupang.springframework.data.requery.domain.functional

import com.coupang.kotlinx.core.hashOf
import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.core.EntityState
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*
import io.requery.converter.LocalDateConverter
import io.requery.query.MutableResult
import java.net.URL
import java.time.LocalDate
import java.util.*

@Entity
abstract class AbstractFuncPerson: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    @get:Column(name = "personId")
    abstract val id: Long?


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

    @get:OneToOne(mappedBy = "person", cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    @get:ForeignKey
    abstract var address: AbstractFuncAddress?

    @get:OneToMany(mappedBy = "owner", cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    abstract val phoneNumbers: MutableResult<AbstractFuncPhone>

    @get:OneToMany(mappedBy = "owner")
    abstract val phoneNumberSet: Set<AbstractFuncPhone>

    @get:OneToMany
    abstract val phoneNumberList: List<AbstractFuncPhone>

    @get:ManyToMany(mappedBy = "members")
    abstract val groups: MutableResult<AbstractFuncGroup>

    @get:ManyToMany(mappedBy = "owners")
    abstract val ownedGroups: MutableResult<AbstractFuncGroup>

    @get:ManyToMany(mappedBy = "personId")
    @get:JunctionTable
    abstract val friends: MutableResult<AbstractFuncPerson>

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

    @get:Transient
    var previousState: EntityState = EntityState.PRE_SAVE

    @get:Transient
    var currentState: EntityState = EntityState.PRE_SAVE

    private fun setState(state: EntityState) {
        previousState = currentState
        currentState = state
    }

    @PreInsert
    fun onPreInsert() {
        setState(EntityState.PRE_SAVE)
    }

    @PostInsert
    fun onPostInsert() {
        setState(EntityState.POST_SAVE)
    }

    @PostLoad
    fun onPostLoad() {
        setState(EntityState.POST_LOAD)
    }

    @PreUpdate
    fun onPreUpdate() {
        setState(EntityState.PRE_UPDATE)
    }

    @PostUpdate
    fun onPostUpdate() {
        setState(EntityState.POST_UPDATE)
    }

    @PreDelete
    fun onPreDelete() {
        setState(EntityState.PRE_DELETE)
    }

    @PostDelete
    fun onPostDelete() {
        setState(EntityState.POST_DELETE)
    }
}