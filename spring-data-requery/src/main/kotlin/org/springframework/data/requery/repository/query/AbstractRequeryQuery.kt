package org.springframework.data.requery.repository.query

import io.requery.query.Result
import io.requery.query.Tuple
import io.requery.query.element.QueryElement
import mu.KotlinLogging
import org.springframework.data.repository.query.QueryMethod
import org.springframework.data.repository.query.RepositoryQuery
import org.springframework.data.repository.query.ReturnedType
import org.springframework.data.requery.core.RequeryOperations
import org.springframework.data.requery.utils.RequeryMetamodel

/**
 * Abstract base class to implement [RepositoryQuery]s.
 *
 * @author debop@coupang.com
 */
abstract class AbstractRequeryQuery(protected val queryMethod: RequeryQueryMethod,
                                    protected val operations: RequeryOperations): RepositoryQuery {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    protected val metamodel = RequeryMetamodel(operations.entityModel)
    protected val domainClass = queryMethod.entityInformation.javaType as Class<out Any>

    override fun getQueryMethod(): QueryMethod = queryMethod

    override fun execute(parameters: Array<Any?>): Any? = doExecute(getExecution(), parameters)

    private fun doExecute(execution: RequeryQueryExecution, values: Array<Any?>): Any? {

        val result = execution.execute(this, values)
        // 필요없는데 ???
        //        val accessor = ParamtersParameterAccessor(queryMethod.parameters, values)

        log.debug { "doExecute ... result=[$result]" }
        return result
    }

    protected open fun getExecution(): RequeryQueryExecution {

        return when {
            queryMethod.isStreamQuery -> StreamExecution(queryMethod.parameters)
            queryMethod.isCollectionQuery -> CollectionExecution()
            queryMethod.isSliceQuery -> SlicedExecution(queryMethod.parameters)
            queryMethod.isPageQuery -> PagedExecution(queryMethod.parameters)
            else -> SingleEntityExecution()
        }
    }

    protected abstract fun doCreateQuery(values: Array<Any?>): QueryElement<out Any>

    protected abstract fun doCreateCountQuery(values: Array<Any?>): QueryElement<out Result<Int>>

    internal fun createQueryElement(values: Array<Any?>): QueryElement<out Any> {
        log.debug { "Create QueryElement with domainClass=${domainClass.name}, values=$values" }
        return doCreateQuery(values)
    }

    protected fun createCountQueryElement(values: Array<Any?>): QueryElement<out Result<Int>> {
        return doCreateCountQuery(values)
    }

    protected fun getTypeToRead(returnedType: ReturnedType): Class<*>? {
        return when {
            returnedType.isProjecting && !metamodel.isRequeryManaged(returnedType.returnedType) -> Tuple::class.java
            else -> null
        }
    }
}