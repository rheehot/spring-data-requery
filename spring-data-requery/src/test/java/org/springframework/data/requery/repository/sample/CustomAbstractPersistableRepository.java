package org.springframework.data.requery.repository.sample;

import org.springframework.data.requery.domain.sample.CustomAbstractPersistable;
import org.springframework.data.requery.repository.RequeryRepository;

/**
 * CustomAbstractPersistableRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
public interface CustomAbstractPersistableRepository extends RequeryRepository<CustomAbstractPersistable, Long> {
}
