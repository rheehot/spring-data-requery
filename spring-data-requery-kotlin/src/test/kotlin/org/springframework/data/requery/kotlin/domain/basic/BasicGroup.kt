package org.springframework.data.requery.kotlin.domain.basic

import io.requery.*
import org.springframework.data.requery.kotlin.converters.ByteArrayToBlobConverter
import java.io.Serializable


@Entity
@Table(name = "basic_group")
interface BasicGroup: Persistable, Serializable {

    @get:Key
    @get:Generated
    val id: Int

    @get:Column(unique = true)
    var name: String

    var description: String

    @get:Convert(ByteArrayToBlobConverter::class)
    var picture: ByteArray

    @get:JunctionTable
    @get:ManyToMany
    val members: MutableSet<BasicUser>

}