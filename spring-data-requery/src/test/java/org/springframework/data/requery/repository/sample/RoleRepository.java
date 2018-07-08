package org.springframework.data.requery.repository.sample;

import io.requery.query.Result;
import io.requery.query.Return;
import org.springframework.data.requery.domain.sample.Role;
import org.springframework.data.requery.repository.RequeryRepository;

import java.util.List;
import java.util.Optional;

/**
 * RoleRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
public interface RoleRepository extends RequeryRepository<Role, Integer> {

    List<Role> findAll();

    Optional<Role> findById(Integer id);


    @Override
    Optional<Role> findOne(Return<? extends Result<Role>> whereClause);


    Long countByName(String name);
}
