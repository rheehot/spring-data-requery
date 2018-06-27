package com.coupang.springframework.data.requery.repository.custom;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.repository.support.RequeryEntityInformation;
import com.coupang.springframework.data.requery.repository.support.SimpleRequeryRepository;
import org.jetbrains.annotations.NotNull;

/**
 * com.coupang.springframework.data.requery.repository.custom.CustomGenericRequeryRepository
 *
 * @author debop
 * @since 18. 6. 9
 */
public class CustomGenericRequeryRepository<T, ID> extends SimpleRequeryRepository<T, ID> implements CustomGenericRepository<T, ID> {

    public CustomGenericRequeryRepository(@NotNull RequeryEntityInformation<T, ID> entityInformation,
                                          @NotNull RequeryOperations operations) {
        super(entityInformation, operations);
    }

    @Override
    public T customMethod(ID id) {
        throw new UnsupportedOperationException("Forced exception for testing purposes");
    }
}
