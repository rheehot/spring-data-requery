package com.coupang.springframework.data.requery.domain.sample

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.Entity

/**
 * AbstractPerson
 *
 * @author debop@coupang.com
 * @since 18. 5. 30
 */
@Entity(copyable = true, cacheable = true)
abstract class AbstractPerson: AbstractPersistable<Long>() {

    abstract override fun getId(): Long?

}