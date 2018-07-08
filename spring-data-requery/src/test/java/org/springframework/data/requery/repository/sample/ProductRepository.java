package org.springframework.data.requery.repository.sample;

import org.springframework.data.requery.domain.sample.Product;
import org.springframework.data.requery.repository.RequeryRepository;

/**
 * ProductRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
public interface ProductRepository extends RequeryRepository<Product, Long> {
}
