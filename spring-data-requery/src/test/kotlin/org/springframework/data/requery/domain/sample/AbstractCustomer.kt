package org.springframework.data.requery.domain.sample

import io.requery.Entity
import io.requery.Key
import org.springframework.data.requery.domain.AbstractPersistable
import org.springframework.data.requery.domain.ToStringBuilder
import java.util.*

@Entity
abstract class AbstractCustomer: AbstractPersistable<Long>() {

    @get:Key
    abstract override val id: Long

    abstract var name: String

    override fun hashCode(): Int = Objects.hashCode(name)

    // NOTE: buildStringHelder 도 메소드이므로 @Transient 를 꼭 지정해줘야 한다.
    @io.requery.Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("name", name)
    }
}