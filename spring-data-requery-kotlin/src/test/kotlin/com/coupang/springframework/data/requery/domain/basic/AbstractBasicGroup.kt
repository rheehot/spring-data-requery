package com.coupang.springframework.data.requery.domain.basic

import com.coupang.kotlinx.core.hashOf
import com.coupang.kotlinx.data.requery.conveters.ByteArrayBlobConverter
import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*
import io.requery.query.MutableResult

@Entity(cacheable = false, copyable = true)
abstract class AbstractBasicGroup: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    @get:Column(name = "groupId")
    abstract val id: Long?

    @get:Column(nullable = false, unique = true)
    abstract var name: String

    abstract var description: String?

    @get:Convert(ByteArrayBlobConverter::class)
    abstract var picture: ByteArray?

    @get:JunctionTable
    @get:ManyToMany
    abstract val members: MutableResult<AbstractBasicUser>

    override fun hashCode(): Int {
        return hashOf(name)
    }

    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("name", name)
    }
}