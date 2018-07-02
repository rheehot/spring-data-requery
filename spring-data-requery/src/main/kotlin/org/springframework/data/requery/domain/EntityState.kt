package org.springframework.data.requery.domain

/**
 * EntityState
 *
 * @author debop
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