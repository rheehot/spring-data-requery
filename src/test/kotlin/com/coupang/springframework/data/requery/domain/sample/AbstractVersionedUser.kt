package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*

/**
 * AbstractVersionedUser
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity
@Table(name = "Versioned_User")
abstract class AbstractVersionedUser: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    @get:Column(name = "user_attr")
    abstract var attribute: String?

    @get:Column(name = "user_name")
    abstract var name: String?

    // NOTE: Superclass 인 경우에는 version 에 final 이 기본이지만, @Entity의 경우에는 `final`을 지정해주어야 합니다.
    @Version
    @JvmField
    final var version: Int = 0

}