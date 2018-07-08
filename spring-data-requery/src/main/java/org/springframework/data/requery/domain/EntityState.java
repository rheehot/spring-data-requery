package org.springframework.data.requery.domain;

/**
 * EntityState
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
public enum EntityState {

    PRE_SAVE,
    POST_SAVE,
    PRE_DELETE,
    POST_DELETE,
    PRE_UPDATE,
    POST_UPDATE,
    POST_LOAD

}
