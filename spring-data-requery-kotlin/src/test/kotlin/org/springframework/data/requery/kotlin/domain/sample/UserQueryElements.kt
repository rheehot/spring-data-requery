package org.springframework.data.requery.kotlin.domain.sample

import io.requery.query.NamedExpression
import io.requery.query.Result
import io.requery.query.element.QueryElement
import org.springframework.data.requery.kotlin.core.RequeryOperations
import org.springframework.data.requery.kotlin.unwrap

/**
 * org.springframework.data.requery.kotlin.domain.sample.UserQueryElements
 *
 * @author debop
 */
object UserQueryElements {

    private fun RequeryOperations.simplePropertyQueryElement(propertyName: String, propertyValue: String): QueryElement<out Result<User>> {

        val filter = NamedExpression.ofString(propertyName).eq(propertyValue)
        return select(User::class).where(filter).unwrap()
    }

    fun RequeryOperations.userHasFirstname(firstname: String): QueryElement<out Result<User>> {
        return simplePropertyQueryElement("firstname", firstname)
    }

    fun RequeryOperations.userHasFirstnameLike(expression: String): QueryElement<out Result<User>> {
        val filter = NamedExpression.ofString("firstname").like("%$expression%")
        return select(User::class).where(filter).unwrap()
    }

    fun RequeryOperations.userHasLastname(lastname: String): QueryElement<out Result<User>> {
        return simplePropertyQueryElement("lastname", lastname)
    }

    fun RequeryOperations.userHashLastnameLikeWithSort(expression: String): QueryElement<out Result<User>> {
        val filter = NamedExpression.ofString("lastname").like("%$expression%")
        val sort = NamedExpression.ofString("firstname").asc()

        return select(User::class).where(filter).orderBy(sort).unwrap()
    }
}