package com.coupang.springframework.data.requery.domain.sample

import io.requery.Embedded

/**
 * com.coupang.springframework.data.requery.domain.sample.AbstractAddress
 * @author debop
 * @since 18. 5. 30
 */
@Embedded
abstract class AbstractAddress {

    abstract var country: String?
    abstract var city: String?
    abstract var streetName: String?
    abstract var streetNo: String?
}