package org.springframework.data.requery.kotlin.domain.sample

import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import org.springframework.data.requery.kotlin.domain.AbstractPersistable
import org.springframework.data.requery.kotlin.domain.ToStringBuilder
import java.util.*

/**
 * org.springframework.data.requery.kotlin.domain.sample.AbstractSite
 *
 * @author debop
 */
@Entity
abstract class AbstractSite: AbstractPersistable<Int>() {

    @get:Key
    @get:Generated
    abstract override val id: Int

    abstract var name: String


    override fun hashCode(): Int = Objects.hashCode(name)

    @io.requery.Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("name", name)
    }
}