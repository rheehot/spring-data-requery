package org.springframework.data.requery.kotlin.domain.sample

/**
 * User 가 Entity라면 이렇게 상속하는 방식은 지원하지 않습니다. `@Superclass` interface라면 상속이 가능합니다.
 *
 * @author debop
 */
//@Entity
//@Table(name = "SpecialUser")
interface SpecialUer: User {
}