package org.springframework.data.requery.repository.sample;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.requery.domain.sample.Dummy;
import org.springframework.transaction.annotation.Transactional;

/**
 * DummyRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
@Transactional
public interface DummyRepository extends CrudRepository<Dummy, Long> {

    // 모두 procedure 테스트용 예제이다. 
}
