package org.springframework.data.requery.repository.query

import io.requery.query.Result
import io.requery.query.element.QueryElement
import org.springframework.data.repository.query.QueryMethod
import org.springframework.data.requery.core.RequeryOperations

/**
 * PartTreeRequeryQuery
 *
 * @author debop@coupang.com
 */
class PartTreeRequeryQuery(method: RequeryQueryMethod, operations: RequeryOperations): AbstractRequeryQuery(method, operations) {

    override fun doCreateQuery(values: Array<Any?>): QueryElement<*> {
        TODO("not implemented")
    }

    override fun doCreateCountQuery(values: Array<Any?>): QueryElement<out Result<Int>> {
        TODO("not implemented")
    }

    override fun getQueryMethod(): QueryMethod {
        TODO("not implemented")
    }
}