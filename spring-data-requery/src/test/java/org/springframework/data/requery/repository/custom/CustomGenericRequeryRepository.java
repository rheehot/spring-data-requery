package org.springframework.data.requery.repository.custom;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.core.RequeryOperations;
import org.springframework.data.requery.repository.support.RequeryEntityInformation;
import org.springframework.data.requery.repository.support.SimpleRequeryRepository;

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
