package com.coupang.springframework.data.requery.java.cache;

import com.coupang.springframework.data.requery.java.domain.AbstractDomainTest;
import com.coupang.springframework.data.requery.java.domain.basic.BasicUser;
import io.requery.EntityCache;
import io.requery.cache.EntityCacheBuilder;
import io.requery.meta.EntityModel;
import org.junit.Before;
import org.junit.Test;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EntityCacheTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 5
 */
public class EntityCacheTest extends AbstractDomainTest {

    @Inject
    EntityModel entityModel;

    private EntityCache cache;

    private EntityCache getCache(EntityModel entityModel) {
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();

        return new EntityCacheBuilder(entityModel)
            .useReferenceCache(false)
            .useSerializableCache(true)
            .useCacheManager(cacheManager)
            .build();
    }

    @Before
    public void setup() {
        if (cache == null) {
            cache = getCache(entityModel);
        }
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
