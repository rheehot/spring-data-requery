package org.springframework.data.requery.repository.sample;

import org.springframework.data.requery.domain.sample.Site;
import org.springframework.data.requery.repository.RequeryRepository;

/**
 * SiteRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
public interface SiteRepository extends RequeryRepository<Site, Integer> {
}
