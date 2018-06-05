package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import io.requery.Table

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractCustomAbstractPersistable
 * @author debop
 * @since 18. 5. 30
 */
@Entity
@Table(name = "customAbstractPersistable")
abstract class AbstractCustomAbstractPersistable: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract val id: Long?

    // NOTE: Id 만 있는 Entity는 Insert 구문이 제대로 생성되지 않습니다.
    abstract var attr: String?

}