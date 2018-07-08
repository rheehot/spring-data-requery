package org.springframework.data.requery.repository.sample;

import io.requery.proxy.CompositeKey;
import org.springframework.data.requery.domain.sample.Item;
import org.springframework.data.requery.repository.RequeryRepository;

/**
 * ItemRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
public interface ItemRepository extends RequeryRepository<Item, CompositeKey<Integer>> {
}
