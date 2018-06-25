package com.coupang.springframework.data.requery.repository.sample;

import com.coupang.springframework.data.requery.domain.sample.ConcreteType1;
import org.springframework.context.annotation.Lazy;

/**
 * ConcreteRepository1
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Lazy
public interface ConcreteRepository1 extends MappedTypeRepository<ConcreteType1> {
}
