package org.springframework.data.requery.kotlin.domain.hierarchy.tree

import io.requery.Column
import io.requery.Embedded
import io.requery.Persistable
import java.io.Serializable

/**
 * org.springframework.data.requery.kotlin.domain.hierarchy.tree.NodePosition
 *
 * @author debop
 */
@Embedded
interface NodePosition: Persistable, Serializable {

    @get:Column
    var nodeLevel: Int

    @get:Column
    var nodeOrder: Int

}