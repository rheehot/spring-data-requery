package org.springframework.data.requery.kotlin.domain.sample

import io.requery.Entity
import io.requery.Key
import io.requery.ManyToOne
import org.springframework.data.requery.kotlin.domain.PersistableObject

/**
 * org.springframework.data.requery.kotlin.domain.sample.ItemSite
 *
 * @author debop
 */
@Entity
interface ItemSite: PersistableObject {

    @get:Key
    @get:ManyToOne
    var item: Item

    @get:Key
    @get:ManyToOne
    var site: Site

}