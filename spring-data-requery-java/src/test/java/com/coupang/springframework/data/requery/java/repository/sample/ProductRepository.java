package com.coupang.springframework.data.requery.java.repository.sample;

import com.coupang.springframework.data.requery.java.domain.sample.Product;
import com.coupang.springframework.data.requery.repository.RequeryRepository;

/**
 * ProductRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
public interface ProductRepository extends RequeryRepository<Product, Long> {
}
