package com.coupang.springframework.data.requery.repository.sample;

import com.coupang.springframework.data.requery.domain.sample.ItemSite;
import com.coupang.springframework.data.requery.repository.RequeryRepository;
import io.requery.proxy.CompositeKey;

/**
 * ItemSiteRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
public interface ItemSiteRepository extends RequeryRepository<ItemSite, CompositeKey<?>> {
}
