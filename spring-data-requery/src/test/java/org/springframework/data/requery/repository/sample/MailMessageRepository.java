package org.springframework.data.requery.repository.sample;

import org.springframework.data.requery.domain.sample.MailMessage;
import org.springframework.data.requery.repository.RequeryRepository;

/**
 * MailMessageRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
public interface MailMessageRepository extends RequeryRepository<MailMessage, Long> {
}
