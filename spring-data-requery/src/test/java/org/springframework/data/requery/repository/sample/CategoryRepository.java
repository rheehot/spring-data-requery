package org.springframework.data.requery.repository.sample;

import org.springframework.data.requery.domain.sample.Category;
import org.springframework.data.requery.repository.RequeryRepository;

/**
 * CategoryRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
public interface CategoryRepository extends RequeryRepository<Category, Long> {
}
