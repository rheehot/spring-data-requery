package com.coupang.springframework.data.requery.repository.sample;

import com.coupang.springframework.data.requery.domain.sample.Item;
import com.coupang.springframework.data.requery.repository.RequeryRepository;
import io.requery.proxy.CompositeKey;
import io.requery.query.Tuple;

/**
 * ItemRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
public interface ItemRepository extends RequeryRepository<Item, CompositeKey<Integer>> {
}
