package com.coupang.springframework.data.requery.cache

import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import com.coupang.springframework.data.requery.domain.Models
import com.coupang.springframework.data.requery.domain.functional.FuncPerson
import com.coupang.springframework.data.requery.domain.functional.RandomData
import io.requery.EntityCache
import io.requery.cache.EntityCacheBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import javax.cache.Caching


/**
 * com.coupang.springframework.data.requery.cache.EntityCacheTest
 * @author debop
 * @since 18. 6. 2
 */
class EntityCacheTest: AbstractDomainTest() {

    private val cache: EntityCache by lazy {
        val provider = Caching.getCachingProvider()
        val cacheManager = provider.cacheManager

        EntityCacheBuilder(Models.DEFAULT)
            .useReferenceCache(false)
            .useSerializableCache(true)
            .useCacheManager(cacheManager)
            .build()
    }

    @Test
    fun `serialize entity and get put`() {

        val p = RandomData.randomPerson()

        val id = 100
        cache.put(FuncPerson::class.java, id, p)

        val d = cache.get(FuncPerson::class.java, id)

        assertThat(d).isNotNull
        assertThat(d === p).isFalse()
        assertThat(d).isEqualTo(p)
    }
}