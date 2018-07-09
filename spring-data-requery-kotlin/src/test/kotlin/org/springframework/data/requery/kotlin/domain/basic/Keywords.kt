package org.springframework.data.requery.kotlin.domain.basic

import io.requery.*
import mu.KotlinLogging
import java.io.Serializable

@Entity
@Table(name = "basic_keyword")
interface Keywords: Persistable, Serializable {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    @get:Key
    @get:Generated
    var id: Int

    var isNotJvmKeywords: String

    var isNewKeyword: Boolean
    var isDefaultKeyword: Boolean

    var getAbstract: String

}