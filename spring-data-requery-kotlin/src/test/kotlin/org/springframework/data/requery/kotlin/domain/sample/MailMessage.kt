package org.springframework.data.requery.kotlin.domain.sample

import io.requery.*
import org.springframework.data.requery.kotlin.domain.PersistableObject

/**
 * org.springframework.data.requery.kotlin.domain.sample.MailMessage
 *
 * @author debop
 */
@Entity
interface MailMessage: PersistableObject {

    @get:Key
    @get:Generated
    val id: Long

    var content: String

    @get:ForeignKey
    @get:OneToOne
    var mailSender: MailSender?
}