package com.coupang.springframework.data.requery.repository.sample;

import com.coupang.springframework.data.requery.domain.sample.Category;
import com.coupang.springframework.data.requery.repository.RequeryRepository;

/**
 * CategoryRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
public interface CategoryRepository extends RequeryRepository<Category, Long> {
}
