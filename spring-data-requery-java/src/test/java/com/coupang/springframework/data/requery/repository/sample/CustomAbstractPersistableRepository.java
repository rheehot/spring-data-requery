package com.coupang.springframework.data.requery.repository.sample;

import com.coupang.springframework.data.requery.domain.sample.CustomAbstractPersistable;
import com.coupang.springframework.data.requery.repository.RequeryRepository;

/**
 * CustomAbstractPersistableRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
public interface CustomAbstractPersistableRepository extends RequeryRepository<CustomAbstractPersistable, Long> {
}
