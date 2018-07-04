package org.springframework.data.requery.repository.query

import io.requery.query.LogicalCondition
import io.requery.query.element.QueryElement
import org.springframework.data.domain.Sort
import org.springframework.data.repository.query.ReturnedType
import org.springframework.data.repository.query.parser.PartTree
import org.springframework.data.requery.core.RequeryOperations

/**
 * org.springframework.data.requery.repository.query.RequeryCountQueryCreator
 *
 * @author debop
 */
class RequeryCountQueryCreator(operations: RequeryOperations,
                               provider: ParameterMetadataProvider,
                               returnedType: ReturnedType,
                               tree: PartTree): RequeryQueryCreator(operations, provider, returnedType, tree) {

    override fun createQueryElement(type: ReturnedType): QueryElement<out Any> {
        TODO("Not implemented")
    }

    override fun complete(criteria: LogicalCondition<out Any, *>?,
                          sort: Sort,
                          base: QueryElement<out Any>): QueryElement<out Any> {
        TODO("Not implemented")
    }
}