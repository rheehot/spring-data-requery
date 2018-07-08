package org.springframework.data.requery.repository.sample;

import io.requery.proxy.CompositeKey;
import org.springframework.data.requery.domain.sample.ItemSite;
import org.springframework.data.requery.repository.RequeryRepository;

/**
 * ItemSiteRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
public interface ItemSiteRepository extends RequeryRepository<ItemSite, CompositeKey<?>> {
}
