package com.coupang.springframework.data.requery.repository.support

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.core.RequeryOperations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport

/**
 * RequeryRepositoryFactoryBean
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
class RequeryRepositoryFactoryBean<T: Repository<S, ID>, S, ID> @Autowired constructor(
    repositoryInterface: Class<out T>
): TransactionalRepositoryFactoryBeanSupport<T, S, ID>(repositoryInterface) {

    companion object: KLogging()

    @Autowired var requeryOperations: RequeryOperations? = null

    override fun doCreateRepositoryFactory(): RepositoryFactorySupport {
        check(requeryOperations != null) { "requeryOperation is not null!" }

        return RequeryRepositoryFactory(requeryOperations!!)
    }

}