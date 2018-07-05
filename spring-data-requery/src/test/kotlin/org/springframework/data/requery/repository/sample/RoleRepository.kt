package org.springframework.data.requery.repository.sample

import org.springframework.data.requery.domain.sample.Role
import org.springframework.data.requery.repository.RequeryRepository

/**
 * RoleRepository
 *
 * @author debop@coupang.com
 */
interface RoleRepository: RequeryRepository<Role, Int> {
}