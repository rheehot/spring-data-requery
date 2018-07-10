package org.springframework.data.requery.kotlin.domain.sample

import io.requery.*
import org.springframework.data.requery.kotlin.domain.PersistableObject

/**
 * org.springframework.data.requery.kotlin.domain.sample.MailSender
 *
 * @author debop
 */
@Entity
interface MailSender: PersistableObject {

    @get:Key
    @get:Generated
    val id: Long

    var name: String

    @get:ForeignKey
    @get:ManyToOne
    var mailUser: MailUser?
}