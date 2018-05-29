package com.coupang.springframework.data.requery.repository.query

import com.coupang.springframework.data.requery.core.RequeryOperations

/**
 * DerivedRequeryQuery
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
class DerivedRequeryQuery(method: RequeryQueryMethod,
                          requeryOp: RequeryOperations): AbstractRequeryQuery(method, requeryOp) {


}