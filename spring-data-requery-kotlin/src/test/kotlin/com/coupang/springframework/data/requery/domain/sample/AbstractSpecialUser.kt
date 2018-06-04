package com.coupang.springframework.data.requery.domain.sample

import io.requery.Entity

/**
 * AbstractSpecialUser
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity
abstract class AbstractSpecialUser: User() {

    abstract var isSpecial: Boolean?

}