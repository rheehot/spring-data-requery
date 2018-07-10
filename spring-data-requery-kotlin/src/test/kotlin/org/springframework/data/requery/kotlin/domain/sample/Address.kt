package org.springframework.data.requery.kotlin.domain.sample

import io.requery.Embedded
import org.springframework.data.requery.kotlin.domain.PersistableObject

/**
 * org.springframework.data.requery.kotlin.domain.sample.Address
 *
 * @author debop
 */
@Embedded
interface Address: PersistableObject {

    var country: String?
    var city: String?
    var streetName: String?
    var streetNo: String?
}