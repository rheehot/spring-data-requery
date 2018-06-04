package com.coupang.springframework.data.requery.domain.sample

import io.requery.Column
import io.requery.Entity
import io.requery.Key
import io.requery.Persistable

/**
 * Composite Key 를 나타내려면 아래와 같이 @Key 를 여러 property에 지정해주면 됩니다.
 */
@Entity
abstract class AbstractItem: Persistable {

    @get:Key
    @get:Column(name = "item_id")
    abstract val id: Int?

    @get:Key
    @get:Column(name = "manufacturer_id")
    abstract val manufacturerId: Int?

    abstract var name: String?
}