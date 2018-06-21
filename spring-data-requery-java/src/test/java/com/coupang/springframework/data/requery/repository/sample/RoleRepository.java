package com.coupang.springframework.data.requery.repository.sample;

import com.coupang.springframework.data.requery.domain.sample.Role;
import com.coupang.springframework.data.requery.repository.RequeryRepository;

/**
 * RoleRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
public interface RoleRepository extends RequeryRepository<Role, Integer> {
}
