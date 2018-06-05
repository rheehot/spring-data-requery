package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractMailSender
 * @author debop
 * @since 18. 5. 30
 */
@Entity
abstract class AbstractMailSender: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract val id: Long?

    abstract var name: String?

    @get:ManyToOne
    @get:Lazy
    abstract var mailUser: AbstractMailUser?

}