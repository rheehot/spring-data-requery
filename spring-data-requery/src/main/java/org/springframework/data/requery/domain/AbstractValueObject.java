package org.springframework.data.requery.domain;

import io.requery.Persistable;

/**
 * org.springframework.data.requery.domain.AbstractValueObject
 *
 * @author debop
 */
public class AbstractValueObject implements ValueObject, Persistable {

    private static final long serialVersionUID = 7365523660535037710L;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return buildStringHelper().toString();
    }

    public String toString(int limit) {
        return buildStringHelper().toString(limit);
    }

    protected ToStringBuilder buildStringHelper() {
        return ToStringBuilder.of(this);
    }
}
