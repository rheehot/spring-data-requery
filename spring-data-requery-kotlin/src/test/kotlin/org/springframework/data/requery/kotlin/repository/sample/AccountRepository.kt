package org.springframework.data.requery.repository.sample

import org.springframework.data.requery.kotlin.domain.sample.Accounts
import org.springframework.data.requery.repository.RequeryRepository

/**
 * AccountRepository
 *
 * @author debop@coupang.com
 */
interface AccountRepository: RequeryRepository<Accounts, Long> {
}