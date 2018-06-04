package com.coupang.springframework.data.requery.domain

import com.coupang.kotlinx.objectx.AbstractValueObject
import com.coupang.kotlinx.objectx.ToStringBuilder
import io.requery.Superclass
import io.requery.Transient
import java.util.*

/**
 * Requery Entity의 최상위 추상화 클래스입니다.
 *
 * 이 클래스를 상속받은 클래스는 반드시 hashCode() 를 재정의해주셔야 합니다.
 *
 * @author debop
 * @since 18. 5. 23
 */
@Superclass
abstract class AbstractPersistable<ID>: AbstractValueObject(), io.requery.Persistable {

    abstract val id: ID?

    @io.requery.Transient
    fun isNew(): Boolean = id == null

    override fun equals(other: Any?): Boolean {
        if(this === other) return true

        return when(other) {
            is AbstractPersistable<*> ->
                if(isNew() && other.isNew()) hashCode() == other.hashCode()
                else Objects.equals(id, other.id)
            else                      -> false
        }
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: System.identityHashCode(this)
    }

    @Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("id", id)
    }
}