package org.springframework.data.requery.domain.sample

import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import io.requery.Table
import org.springframework.data.requery.domain.AbstractPersistable
import org.springframework.data.requery.domain.ToStringBuilder
import java.util.*

@Entity
@Table(name = "SD_Roles")
abstract class AbstractRole: AbstractPersistable<Int>() {

    @get:Key
    @get:Generated
    abstract override val id: Int

    abstract var name: String

    override fun hashCode(): Int {
        return Objects.hashCode(name)
    }

    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("name", name)
    }
}