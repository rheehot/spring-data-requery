package com.coupang.springframework.data.requery.repository.query

import com.coupang.springframework.data.requery.core.RequeryOperations
import org.springframework.data.repository.query.QueryMethod
import org.springframework.data.repository.query.RepositoryQuery

/**
 * AbstractRequeryQuery
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
abstract class AbstractRequeryQuery(private val method: RequeryQueryMethod,
                                    private val operations: RequeryOperations): RepositoryQuery {

    private val domainClass: Class<*> = method.entityInformation.javaType

    override fun getQueryMethod(): QueryMethod = method

    override fun execute(parameters: Array<out Any>?): Any {
        TODO("not implemented")
    }

    protected fun createQuery(accessor: RequeryParameterAccessor,
                              bindVars: Map<String, Any?>): String {
        TODO("not implemented")
    }
}