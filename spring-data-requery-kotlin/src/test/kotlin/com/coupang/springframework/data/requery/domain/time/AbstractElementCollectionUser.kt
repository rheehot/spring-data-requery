package com.coupang.springframework.data.requery.domain.time

import com.coupang.kotlinx.core.hashOf
import com.coupang.kotlinx.objectx.AbstractValueObject
import com.coupang.kotlinx.objectx.ToStringBuilder
import io.requery.Entity
import io.requery.Key
import io.requery.Persistable
import java.net.URL
import java.util.*

/**
 * AbstractElementCollectionUser
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity
abstract class AbstractElementCollectionUser: AbstractValueObject(), Persistable {

    @get:Key
    abstract var id: UUID

    @get:Key
    abstract var name: String

    abstract var age: Int?
    abstract var email: String?

    // HINT: JPA @ElementCollection 과 유사하게 하는 방식은 없다. Set 을 문자열로 한 컬럼에 저장하는 방식이나, OneToMany 를 사용해야 한다.
    //
    abstract var phoneNumbers: MutableSet<String>
    abstract val attributes: MutableMap<String, String>

    abstract var homepage: URL?

    override fun hashCode(): Int {
        return hashOf(id, name)
    }

    @io.requery.Transient
    override fun buildStringHelper(): ToStringBuilder {
        return super.buildStringHelper()
            .add("id", id)
            .add("name", name)
            .add("age", age)
            .add("email", email)
    }
}