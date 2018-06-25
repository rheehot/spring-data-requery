package com.coupang.springframework.data.requery.repository.sample;

import com.coupang.springframework.data.requery.domain.sample.MailMessage;
import com.coupang.springframework.data.requery.repository.RequeryRepository;

/**
 * MailMessageRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 25
 */
public interface MailMessageRepository extends RequeryRepository<MailMessage, Long> {
}
