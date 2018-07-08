package org.springframework.data.requery.kotlin.repository.sample

/**
 * org.springframework.data.requery.repository.sample.NameOnlyDto
 *
 * @author debop
 */
data class NameOnlyDto @JvmOverloads constructor(
    var firstname: String? = null,
    var lastname: String? = null
)