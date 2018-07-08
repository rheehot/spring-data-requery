package org.springframework.data.requery.repository.sample

import org.springframework.data.repository.CrudRepository
import org.springframework.data.requery.kotlin.domain.sample.Dummy
import org.springframework.transaction.annotation.Transactional

/**
 * org.springframework.data.requery.repository.sample.DummyRepository
 *
 * @author debop
 */
@Transactional
interface DummyRepository: CrudRepository<Dummy, Int> {

    // 모두 procedure 테스트용 예제이다.

}