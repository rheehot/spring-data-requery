package com.coupang.springframework.data.requery.core

/**
 * EntityState
 *
 * @author debop@coupang.com
 * @since 18. 6. 1
 */
enum class EntityState {
    PRE_SAVE,
    POST_SAVE,
    PRE_DELETE,
    POST_DELETE,
    PRE_UPDATE,
    POST_UPDATE,
    POST_LOAD
}