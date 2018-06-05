package com.coupang.springframework.data.requery.convert;

import io.requery.Converter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * IntArrayListStringConverter
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
public class IntArrayListStringConverter implements Converter<ArrayList<Integer>, String> {

    @SuppressWarnings("unchecked")
    @Override
    public Class<ArrayList<Integer>> getMappedType() {
        return (Class) ArrayList.class;
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
    public String convertToPersisted(ArrayList<Integer> value) {
        if (value == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (Integer v : value) {
            if (index > 0) {
                sb.append(",");
            }
            sb.append(v);
            index++;
        }
        return sb.toString();
    }

    @Override
    public ArrayList<Integer> convertToMapped(Class<? extends ArrayList<Integer>> type, String value) {
        if (value == null) {
            return null;
        }
        if (StringUtils.isEmpty(value)) {
            return new ArrayList<>();
        }

        ArrayList<Integer> list = new ArrayList<>();

        String[] parts = value.split(",");
        for (String part : parts) {
            list.add(Integer.valueOf(part));
        }

        return list;
    }
}
