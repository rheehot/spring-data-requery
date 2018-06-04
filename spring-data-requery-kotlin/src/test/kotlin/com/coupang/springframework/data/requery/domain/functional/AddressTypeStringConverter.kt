package com.coupang.springframework.data.requery.domain.functional

import io.requery.converter.EnumStringConverter

/**
 * AddressTypeStringConverter
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
class AddressTypeStringConverter: EnumStringConverter<AddressType>(AddressType::class.java)