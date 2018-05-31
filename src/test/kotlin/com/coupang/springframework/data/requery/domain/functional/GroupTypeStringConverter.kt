package com.coupang.springframework.data.requery.domain.functional

import io.requery.converter.EnumStringConverter

/**
 * GroupTypeStringConverter
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
class GroupTypeStringConverter: EnumStringConverter<GroupType>(GroupType::class.java)