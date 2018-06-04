package com.coupang.springframework.data.requery.convert;

import io.requery.Converter;
import org.apache.commons.lang3.StringUtils;

/**
 * IntArrayStringConverter
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
public class IntArrayStringConverter implements Converter<int[], String> {

    @Override
    public Class<int[]> getMappedType() {
        return int[].class;
    }

    @Override
    public Class<String> getPersistedType() {
        return String.class;
    }

    @Override
    public Integer getPersistedSize() {
        return null;
    }

    @Override
    public String convertToPersisted(int[] value) {
        StringBuilder sb = new StringBuilder(value.length);

        for (int x : value) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(x);
        }
        return sb.toString();
    }

    @Override
    public int[] convertToMapped(Class<? extends int[]> type, String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        String[] strs = value.split(",");
        int[] ints = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {
            ints[i] = Integer.valueOf(strs[i]);
        }
        return ints;
    }
}
