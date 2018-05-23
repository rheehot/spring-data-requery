package com.coupang.springframework.data.requery.domain

import org.springframework.data.domain.Persistable
import java.io.Serializable

/**
 * com.coupang.springframework.data.requery.domain.AbstractPersistable
 * @author debop
 * @since 18. 5. 23
 */
abstract class AbstractPersistable<PK: Serializable>: Persistable<PK>, io.requery.Persistable {
}