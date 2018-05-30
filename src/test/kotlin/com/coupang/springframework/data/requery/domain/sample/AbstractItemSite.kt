package com.coupang.springframework.data.requery.domain.sample

import io.requery.Entity
import io.requery.Key
import io.requery.ManyToOne
import io.requery.Persistable

/**
 * Many To Many 를 담당하는 Join table 을 이렇게 entity로 만들 필요없이 Item과 Site 간의 ManyToMany를 지정하는 것이 편하다.
 * 이렇게 하는 것은 JPA 에서 성능을 위해 ManyToOne 만을 가진 Join Table을 표현하기 위해 사용한다.
 *
 * @author debop
 * @since 18. 5. 30
 */
@Entity
abstract class AbstractItemSite: Persistable {
    @get:Key
    @get:ManyToOne
    abstract var item: AbstractItem?

    @get:Key
    @get:ManyToOne
    abstract var site: AbstractSite?

}