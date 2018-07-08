package org.springframework.data.requery.repository.sample;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.requery.domain.sample.Parent;
import org.springframework.data.requery.repository.RequeryRepository;

/**
 * ParentRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Lazy
public interface ParentRepository extends RequeryRepository<Parent, Long> {
}
