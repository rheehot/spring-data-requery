package org.springframework.data.requery.converters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import javax.annotation.Nullable;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * {@link byte[]} 를 {@link Blob}로 저장하도록 하는 Converter
 *
 * @author debop
 */
@Slf4j
public class ByteArrayToBlobConverter implements io.requery.Converter<byte[], Blob> {

    @Override
    public Class<byte[]> getMappedType() {
        return byte[].class;
    }

    @Override
    public Class<Blob> getPersistedType() {
        return Blob.class;
    }

    @Nullable
    @Override
    public Integer getPersistedSize() {
        return null;
    }

    @Override
    public Blob convertToPersisted(byte[] value) {
        try {
            return (value != null) ? new SerialBlob(value) : null;
        } catch (SQLException e) {
            log.error("Fail to convert to Blob.", e);
            return null;
        }
    }

    @Override
    public byte[] convertToMapped(Class<? extends byte[]> type, @Nullable Blob value) {
        try {
            return (value != null) ? StreamUtils.copyToByteArray(value.getBinaryStream()) : null;
        } catch (Exception e) {
            log.error("Fail to convert to byte[]", e);
            return null;
        }
    }
}
