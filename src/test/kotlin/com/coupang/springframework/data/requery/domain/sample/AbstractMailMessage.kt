package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractMailMessage
 * @author debop
 * @since 18. 5. 30
 */
@Entity
abstract class AbstractMailMessage: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    @get:OneToOne(cascade = [CascadeAction.SAVE, CascadeAction.DELETE])
    @get:ForeignKey
    abstract var mailSender: AbstractMailSender?

    abstract var content: String?
}