package org.springframework.data.requery.repository.sample;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.requery.domain.sample.ConcreteType2;

/**
 * ConcreteRepository1
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Lazy
public interface ConcreteRepository2 extends MappedTypeRepository<ConcreteType2> {
}
