package com.coupang.springframework.data.requery.java.domain.blob;

import com.coupang.springframework.data.requery.java.domain.AbstractDomainTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * com.coupang.springframework.data.requery.java.domain.blob.BinaryDataTest
 *
 * @author debop
 * @since 18. 6. 4
 */
public class BinaryDataTest extends AbstractDomainTest {

    @Test
    public void saveBlobData() {
        byte[] bytes = new byte[8192];
        rnd.nextBytes(bytes);

        BinaryData binData = new BinaryData();
        binData.setName("binary data");
        binData.setPicture(bytes);

        requeryTemplate.insert(binData);
        assertThat(binData).isNotNull();

        BinaryData loaded = requeryTemplate.findById(BinaryData.class, binData.id);
        assertThat(loaded).isNotNull().isEqualTo(binData);
        assertThat(loaded.getPicture()).isNotNull().isEqualTo(binData.getPicture());

        rnd.nextBytes(bytes);
        loaded.setPicture(bytes);
        requeryTemplate.upsert(loaded);
    }
}
