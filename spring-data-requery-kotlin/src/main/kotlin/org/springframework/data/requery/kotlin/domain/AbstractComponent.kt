package org.springframework.data.requery.kotlin.domain

import io.requery.Persistable

/**
 * [io.requery.Embedded]에 사용할 Value Object 를 표현하는 클래스의 최상위 클래스
 *
 * @author debop
 */
abstract class AbstractComponent: AbstractValueObject(), Persistable