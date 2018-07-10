package org.springframework.data.requery.kotlin.cache

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.data.requery.kotlin.domain.RandomData
import org.springframework.data.requery.kotlin.domain.sample.Role
import org.springframework.data.requery.kotlin.domain.sample.RoleEntity
import org.springframework.data.requery.kotlin.domain.sample.User

/**
 * Cache2kEntityCacheTest
 *
 * @author debop@coupang.com
 */
class Cache2kEntityCacheTest {

    private val entityCache = Cache2kEntityCache()

    @Before
    fun setup() {
        entityCache.clear()
    }

    @Test
    fun `instancing Cache2kEntityCache`() {

        val user = RandomData.randomUser()
        val userId = 1

        assertThat(entityCache.contains(User::class.java, userId)).isFalse()

        entityCache.put(User::class.java, userId, user)
        assertThat(entityCache.contains(User::class.java, userId)).isTrue()

        val cached = entityCache.get(User::class.java, userId)
        assertThat(cached).isNotNull
        assertThat(cached).isEqualTo(user)
    }

    @Test
    fun `invalidate cache item`() {

        val user = RandomData.randomUser()
        val userId = 1

        entityCache.put(User::class.java, userId, user)
        assertThat(entityCache.contains(User::class.java, userId)).isTrue()

        entityCache.invalidate(User::class.java, userId)
        assertThat(entityCache.contains(User::class.java, userId)).isFalse()
    }

    @Test
    fun `invalidate all`() {

        val user = RandomData.randomUser()
        val userId = 1

        val user2 = RandomData.randomUser()
        val user2Id = 2

        entityCache.put(User::class.java, userId, user)
        entityCache.put(User::class.java, user2Id, user2)
        assertThat(entityCache.contains(User::class.java, userId)).isTrue()
        assertThat(entityCache.contains(User::class.java, user2Id)).isTrue()

        entityCache.invalidate(User::class.java)
        assertThat(entityCache.contains(User::class.java, userId)).isFalse()
        assertThat(entityCache.contains(User::class.java, user2Id)).isFalse()
    }

    @Test
    fun `various entity type caching`() {

        val user = RandomData.randomUser()
        val userId = 1

        val role = RoleEntity()
        val roleId = 1

        entityCache.put(User::class.java, userId, user)
        assertThat(entityCache.contains(User::class.java, userId)).isTrue()

        entityCache.put(Role::class.java, roleId, role)
        assertThat(entityCache.contains(Role::class.java, roleId)).isTrue()


        entityCache.clear()
        assertThat(entityCache.contains(User::class.java, userId)).isFalse()
        assertThat(entityCache.contains(Role::class.java, roleId)).isFalse()
    }
}