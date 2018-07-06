package org.springframework.data.requery.repository.sample

import org.springframework.data.requery.domain.sample.Product
import org.springframework.data.requery.repository.RequeryRepository

/**
 * org.springframework.data.requery.repository.sample.ProductRepository
 *
 * @author debop
 */
interface ProductRepository: RequeryRepository<Product, Long> {
}