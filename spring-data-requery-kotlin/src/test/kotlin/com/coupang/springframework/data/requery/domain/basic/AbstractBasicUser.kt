package com.coupang.springframework.data.requery.domain.basic

import com.coupang.kotlinx.core.hashOf
import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*
import io.requery.converter.LocalDateConverter
import io.requery.query.MutableResult
import java.net.URL
import java.time.LocalDate
import java.util.*


@Entity(cacheable = false, copyable = true)
abstract class AbstractBasicUser: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    @get:Column(name = "userId")
    abstract val id: Long?

    @get:Index(value = ["idx_basic_user_name_email"])
    abstract var name: String

    @get:Index(value = ["idx_basic_user_name_email"])
    abstract var email: String

    @get:Convert(LocalDateConverter::class)
    abstract var birthday: LocalDate?

    @get:Nullable
    abstract var age: Int?

    // NOTE: OneToOne 한 곳은 @ForeignKey 를 지정하고, 다른 한쪽은 mappedBy를 지정해줘야 합니다.
    // NOTE: OneToOne 의 mappedBy 를 지정한 쪽은 abstract 가 아닌 var 만으로 지정해줘야 합니다.  
    @get:ForeignKey
    @get:OneToOne
    abstract var location: AbstractBasicLocation?

    @get:ManyToMany(mappedBy = "members", cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    abstract val groups: MutableResult<AbstractBasicGroup>

    abstract var about: String?

    @get:Column(unique = true)
    abstract var uuid: UUID

    abstract var homepage: URL?

    abstract var picture: String

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