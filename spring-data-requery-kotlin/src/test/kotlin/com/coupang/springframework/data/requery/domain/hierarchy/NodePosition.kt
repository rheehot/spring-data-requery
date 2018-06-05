package com.coupang.springframework.data.requery.domain.hierarchy

import io.requery.Embedded

/**
 * NodePosition
 *
 * @author debop@coupang.com
 * @since 18. 6. 5
 */
@Embedded
interface NodePosition {

    var nodeOrder: Int

    var nodeLevel: Int

}