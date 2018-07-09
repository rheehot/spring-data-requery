package org.springframework.data.requery.kotlin.domain.sample

import io.requery.Column
import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import org.springframework.data.requery.kotlin.domain.AbstractPersistable
import org.springframework.data.requery.kotlin.domain.ToStringBuilder
import java.util.*

/**
 * AbstractAccount
 *
 * @author debop@coupang.com
 */
@Entity(name = "Accounts")
abstract class AbstractAccount: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long

    @get:Column(name = "account_name")
    abstract var name: String


    override fun hashCode(): Int {
        return Objects.hashCode(name)
    }

    // NOTE: buildStringHelder 도 메소드이므로 @Transient 를 꼭 지정해줘야 한다.
    @io.requery.Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("name", name)
    }
}