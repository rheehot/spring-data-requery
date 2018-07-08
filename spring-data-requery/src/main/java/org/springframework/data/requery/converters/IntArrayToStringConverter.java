package org.springframework.data.requery.converters;

import io.requery.Converter;

import javax.annotation.Nullable;

/**
 * org.springframework.data.requery.converters.IntArrayToStringConverter
 *
 * @author debop
 */
public class IntArrayToStringConverter implements Converter<int[], String> {
    @Override
    public Class<int[]> getMappedType() {
        return int[].class;
    }

    @Override
    public Class<String> getPersistedType() {
        return String.class;
    }

    @Nullable
    @Override
    public Integer getPersistedSize() {
        return null;
    }

    @Override
    public String convertToPersisted(int[] value) {
        if (value != null && value.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (int item : value) {
                builder.append(item).append(",");
            }
            return (builder.length() > 0)
                   ? builder.substring(0, builder.length() - 1)
                   : builder.toString();
        }
        return null;
    }

    @Override
    public int[] convertToMapped(Class<? extends int[]> type, @Nullable String value) {
        if (value != null && value.length() > 0) {
            String[] items = value.split(",");
            int[] result = new int[items.length];

            for (int i = 0; i < items.length; i++) {
                result[i] = Integer.valueOf(items[i]);
            }
            return result;
        }

        return null;
    }
}
