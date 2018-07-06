package org.springframework.data.requery.repository.query

import io.requery.query.LogicalCondition
import io.requery.query.element.QueryElement
import io.requery.query.function.Count
import org.springframework.data.domain.Sort
import org.springframework.data.repository.query.ReturnedType
import org.springframework.data.repository.query.parser.PartTree
import org.springframework.data.requery.core.RequeryOperations
import org.springframework.data.requery.unwrap

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
        return operations
            .select(Count.count(type.domainType))
            .unwrap()
    }

    override fun complete(criteria: LogicalCondition<out Any, *>?,
                          sort: Sort,
                          base: QueryElement<out Any>): QueryElement<out Any> {

        return operations
            .select(Count.count(domainClass))
            .where(criteria)
            .unwrap()
    }
}