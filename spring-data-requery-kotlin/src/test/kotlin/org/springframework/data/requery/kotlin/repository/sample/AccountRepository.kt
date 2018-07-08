package org.springframework.data.requery.kotlin.repository.sample

import org.springframework.data.requery.kotlin.domain.sample.Accounts
import org.springframework.data.requery.kotlin.repository.RequeryRepository

/**
 * AccountRepository
 *
 * @author debop@coupang.com
 */
interface AccountRepository: RequeryRepository<Accounts, Long> {
}