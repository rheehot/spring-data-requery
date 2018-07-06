package org.springframework.data.requery.repository.query

import io.requery.query.Result
import io.requery.query.element.QueryElement
import mu.KotlinLogging
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.SliceImpl
import org.springframework.data.repository.query.ReturnedType
import org.springframework.data.requery.NotSupportedException
import org.springframework.data.requery.core.RequeryOperations

/**
 * DeclaredRequeryQuery
 *
 * @author debop@coupang.com
 */
class DeclaredRequeryQuery(queryMethod: RequeryQueryMethod, operations: RequeryOperations)
    : AbstractRequeryQuery(queryMethod, operations) {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    override fun doCreateQuery(values: Array<Any?>): QueryElement<out Any> {
        throw NotSupportedException("Unsupported operation in DeclaredRequeryQuery.")
    }

    override fun doCreateCountQuery(values: Array<Any?>): QueryElement<out Result<Int>> {
        throw NotSupportedException("Unsupported operation in DeclaredRequeryQuery.")
    }

    override fun execute(parameters: Array<Any?>): Any? {

        var query = getNativeQuery()

        log.debug { "Execute query. query=$query, return type=${queryMethod.returnType}" }

        // TODO: Refactoring이 필요하다.
        // TODO: Entity 나 Tuple 에 대해 ReturnedType에 맞게 casting 해야 한다.
        // TODO: Paging 에 대해서는 처리했는데, Sort 는 넣지 못했음. 이 부분도 추가해야 함.

        val accessor = RequeryParametersParameterAccessor(queryMethod.parameters, parameters)
        val pageable = accessor.pageable

        return when {
            pageable.isPaged -> {

                val values = removePageable(accessor, parameters)
                val countQuery = "select count(cnt_tbl.*) from ($query) as cnt_tbl"
                val totals: Int? = operations.raw(countQuery, values).firstOrNull()?.get<Int>(0)

                query = "$query offset ${pageable.offset} limit ${pageable.pageSize}"

                runNativeQuery(query, parameters).castResult(pageable, totals?.toLong())
            }
            else ->
                runNativeQuery(query, parameters).castResult()
        }
    }

    private fun runNativeQuery(query: String, parameters: Array<Any?>): Result<*> = when {
        queryMethod.isQueryForEntity -> {
            log.trace { "Query for entity... entity=${queryMethod.entityClass}" }
            operations.raw(queryMethod.entityClass, query, parameters)
        }
        else -> operations.raw(query, parameters)
    }

    private fun Result<*>.castResult(pageable: Pageable = Pageable.unpaged(),
                                     totals: Long? = null): Any? {
        // TODO: List<Tuple> 인 경우 returned type 으로 변경해야 한다.

        return when {
            queryMethod.isCollectionQuery -> this.toList()
            queryMethod.isStreamQuery -> this.stream()
            queryMethod.isSliceQuery -> when {
                pageable.isPaged && totals != null -> {
                    val hasNext = totals > pageable.offset + pageable.pageSize
                    SliceImpl(this.toList(), pageable, hasNext)
                }
                else -> SliceImpl(this.toList())
            }
            queryMethod.isPageQuery -> when {
                pageable.isPaged && totals != null -> PageImpl(this.toList(), pageable, totals)
                else -> PageImpl(this.toList())
            }
            else -> RequeryResultConverter.convert(this.firstOrNull())
        }
    }

    private fun getNativeQuery(): String {
        val nativeQuery = queryMethod.annotatedQuery
        log.trace { "Get native query. query=$nativeQuery" }

        if(nativeQuery.isNullOrBlank()) {
            error("No `@Query` query specified on ${queryMethod.name}")
        }

        return nativeQuery!!
    }

    private fun getReturnedType(parameters: Array<Any?>): ReturnedType {
        val accessor = RequeryParametersParameterAccessor(queryMethod, parameters)
        val processor = queryMethod.resultProcessor

        val returnedType = processor.withDynamicProjection(accessor).returnedType
        log.trace { "Return type is $returnedType" }

        return returnedType
    }

    // 인자 컬렉션의 Pageable 인스턴스의 index 를 찾아 parameters 에서 제거한다.
    private fun removePageable(accessor: RequeryParametersParameterAccessor, parameters: Array<Any?>): Array<Any?> {

        val pageableIndex = accessor.parameters.pageableIndex

        return parameters
            .withIndex()
            .mapNotNull {
                if(it.index != pageableIndex) parameters[it.index] else null
            }
            .toTypedArray()
    }
}