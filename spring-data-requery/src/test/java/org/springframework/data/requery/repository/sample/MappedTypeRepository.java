package org.springframework.data.requery.repository.sample;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.requery.domain.sample.AbstractMappedType;
import org.springframework.data.requery.repository.RequeryRepository;

import java.util.List;

/**
 * MappedTypeRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@NoRepositoryBean
public interface MappedTypeRepository<T extends AbstractMappedType> extends RequeryRepository<T, Long> {

    // TODO: Spring EL을 이용하여 TABLE Name을 지정할 수 있으면 좋을까?
    // @Query("from #{#entityName} t where t.attribute1=?")
    List<T> findAllByAttribute1(String attribute1);

    // TODO: Spring EL을 이용하여 TABLE Name을 지정할 수 있으면 좋을까?
    // @Query("from #{#entityName} t where t.attribute1=?")
    Page<T> findByAttribute1Custom(String attribute1, Pageable pageable);
}
