package com.coupang.springframework.data.requery.repository.sample;

import com.coupang.springframework.data.requery.domain.sample.Parent;
import com.coupang.springframework.data.requery.repository.RequeryRepository;
import org.springframework.context.annotation.Lazy;

/**
 * ParentRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Lazy
public interface ParentRepository extends RequeryRepository<Parent, Long> {
}
