package org.springframework.data.requery.domain

import java.io.Serializable

/**
 * 값 형식의 데이터를 표현하는 클래스의 기본 인터페이스입니다.
 */
interface ValueObject: Serializable

/**
 * 값 형식의 데이터를 표현하는 클래스의 추상화 클래스
 *
 * @author debop@coupang.com
 */
abstract class AbstractValueObject: ValueObject {

    override fun equals(other: Any?): Boolean = when(other) {
        null -> false
        else -> this === other || (this.javaClass == other.javaClass && hashCode() == other.hashCode())
    }

    override fun hashCode(): Int = System.identityHashCode(this)

    /**
     * Don't override toString method, override buildStringHelper instead
     */
    override fun toString(): String = buildStringHelper().toString()

    /**
     * Don't override toString method, override buildStringHelper instead
     */
    open fun toString(limit: Int): String = buildStringHelper().toString(limit)

    /**
     * build ToStringBuilder for object description string
     **/
    protected open fun buildStringHelper(): ToStringBuilder = ToStringBuilder.of(this)

}
