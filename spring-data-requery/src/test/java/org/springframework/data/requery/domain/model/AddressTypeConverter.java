package org.springframework.data.requery.domain.model;

import io.requery.converter.EnumStringConverter;

/**
 * com.coupang.springframework.data.requery.domain.model.AddressTypeConverter
 *
 * @author debop
 * @since 18. 6. 4
 */
public class AddressTypeConverter extends EnumStringConverter<AddressType> {

    public AddressTypeConverter() {
        super(AddressType.class);
    }

}
