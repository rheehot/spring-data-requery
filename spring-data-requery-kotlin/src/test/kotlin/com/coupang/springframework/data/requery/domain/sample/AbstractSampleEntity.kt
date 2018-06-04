package com.coupang.springframework.data.requery.domain.sample

import com.coupang.kotlinx.core.hashOf
import com.coupang.kotlinx.objectx.AbstractValueObject
import com.coupang.kotlinx.objectx.ToStringBuilder
import io.requery.Entity
import io.requery.Key
import io.requery.Persistable
import io.requery.Transient

/**
 * AbstractSampleEntity
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity
abstract class AbstractSampleEntity: AbstractValueObject(), Persistable {

    @get:Key
    abstract var firstId: String

    @get:Key
    abstract var secondId: String


    abstract var name: String?


    override fun hashCode(): Int {
        return hashOf(firstId, secondId)
    }

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("firstId", firstId)
            .add("secondId", secondId)
    }
}