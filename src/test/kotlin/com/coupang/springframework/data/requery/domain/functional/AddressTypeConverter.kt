package com.coupang.springframework.data.requery.domain.functional

import io.requery.converter.EnumStringConverter

/**
 * AddressTypeConverter
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
class AddressTypeConverter: EnumStringConverter<AddressType>(AddressType::class.java)