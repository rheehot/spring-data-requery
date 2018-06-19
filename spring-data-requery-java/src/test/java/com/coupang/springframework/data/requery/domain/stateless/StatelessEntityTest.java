package com.coupang.springframework.data.requery.domain.stateless;

import com.coupang.springframework.data.requery.domain.AbstractDomainTest;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Diego on 2018. 6. 12..
 */
public class StatelessEntityTest extends AbstractDomainTest {

    @Test
    public void insert_and_delete_stateless() {

        UUID uuid = UUID.randomUUID();

        Entry entry = new Entry();
        entry.setId(uuid.toString());
        entry.setFlag1(true);
        entry.setFlag2(false);

        requeryTemplate.insert(entry);

        Entry found = requeryTemplate.findById(Entry.class, entry.getId());
        assertThat(found.getId()).isEqualTo(entry.getId());
    }
}
