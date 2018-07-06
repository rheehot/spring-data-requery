package org.springframework.data.requery.domain.sample

import io.requery.*
import org.springframework.data.requery.domain.AbstractPersistable

/**
 * org.springframework.data.requery.domain.sample.AbstractCategory
 *
 * @author debop
 */
@Entity
abstract class AbstractCategory: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long

    @get:ForeignKey
    @get:ManyToOne
    abstract var product: AbstractProduct?

}