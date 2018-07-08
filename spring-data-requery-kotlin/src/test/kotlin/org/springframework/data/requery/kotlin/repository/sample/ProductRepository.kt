package org.springframework.data.requery.kotlin.repository.sample

import org.springframework.data.requery.kotlin.domain.sample.Product
import org.springframework.data.requery.kotlin.repository.RequeryRepository

/**
 * org.springframework.data.requery.repository.sample.ProductRepository
 *
 * @author debop
 */
interface ProductRepository: RequeryRepository<Product, Long> {
}