package org.springframework.data.requery.domain.model;

import java.util.function.Supplier;

/**
 * com.coupang.springframework.data.requery.domain.model.PhoneFactory
 *
 * @author debop
 * @since 18. 6. 4
 */
public abstract class PhoneFactory implements Supplier<AbstractPhone> {
    @Override
    public AbstractPhone get() {
        return new Phone("");
    }
}
