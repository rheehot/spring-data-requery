package org.springframework.data.requery.kotlin.domain.sample

import io.requery.Entity
import io.requery.Table

/**
 * org.springframework.data.requery.kotlin.domain.sample.AbstractSpecialUser
 *
 * @author debop
 */
@Entity
@Table(name = "SpecialUser")
abstract class AbstractSpecialUser: User() 