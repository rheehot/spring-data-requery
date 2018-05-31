package com.coupang.springframework.data.requery.domain.functional

import com.coupang.kotlinx.core.hashOf
import com.coupang.kotlinx.objectx.ToStringBuilder
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*
import io.requery.converter.LocalDateTimeConverter
import java.time.LocalDateTime

/**
 * AbstractFuncGroup
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity
abstract class AbstractFuncGroup: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    @get:Column
    abstract var name: String

    @get:Column
    abstract var description: String?

    @get:Convert(GroupTypeStringConverter::class)
    abstract var type: GroupType

    @Version
    @JvmField
    final var version: Int = 0

    @get:ManyToMany
    @get:JunctionTable(name = "Func_Group_Members")
    @get:OrderBy("name")
    abstract val members: MutableSet<AbstractFuncPerson>

    @get:ManyToMany
    @get:JunctionTable(name = "Func_Group_Owners")
    @get:OrderBy("name")
    abstract val owners: MutableSet<AbstractFuncPerson>

    @get:Convert(LocalDateTimeConverter::class)
    abstract var createdAt: LocalDateTime?

    @get:Transient
    var temporaryName: String? = null

    @PreInsert
    fun onPreInsert() {
        createdAt = LocalDateTime.now()
    }

    override fun hashCode(): Int {
        return hashOf(name, type)
    }

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("name", name)
            .add("type", type)
    }
}