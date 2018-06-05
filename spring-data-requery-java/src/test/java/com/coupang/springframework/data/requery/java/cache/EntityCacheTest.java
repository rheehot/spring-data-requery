package com.coupang.springframework.data.requery.java.cache;

import com.coupang.springframework.data.requery.java.domain.AbstractDomainTest;
import com.coupang.springframework.data.requery.java.domain.Models;
import com.coupang.springframework.data.requery.java.domain.basic.BasicUser;
import io.requery.EntityCache;
import io.requery.cache.EntityCacheBuilder;
import org.junit.Test;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EntityCacheTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 5
 */
public class EntityCacheTest extends AbstractDomainTest {

    private static final EntityCache cache = getCache();

    private static EntityCache getCache() {
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();

        return new EntityCacheBuilder(Models.DEFAULT)
            .useReferenceCache(false)
            .useSerializableCache(true)
            .useCacheManager(cacheManager)
            .build();
    }

    @Test
    public void serializeEntityToCache() {
        BasicUser user = new BasicUser();
        int id = 100;

        cache.put(BasicUser.class, id, user);

        BasicUser loaded = cache.get(BasicUser.class, id);

        assertThat(loaded).isNotNull();
        assertThat(loaded).isEqualTo(user);
    }
}
