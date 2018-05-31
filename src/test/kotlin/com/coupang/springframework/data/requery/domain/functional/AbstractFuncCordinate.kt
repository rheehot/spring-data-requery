package com.coupang.springframework.data.requery.domain.functional

import com.coupang.springframework.data.requery.domain.AbstractComponent
import io.requery.Column
import io.requery.Lazy
import io.requery.Superclass

/**
 * AbstractFuncCordinate
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Superclass
abstract class AbstractFuncCordinate: AbstractComponent() {

    @get:Lazy
    @get:Column(value = "0.0", nullable = false)
    abstract var latitude: Float

    @get:Lazy
    @get:Column(value = "0.0", nullable = false)
    abstract var longitude: Float
}