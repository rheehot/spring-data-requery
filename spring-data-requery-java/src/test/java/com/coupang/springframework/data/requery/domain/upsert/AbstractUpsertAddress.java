package com.coupang.springframework.data.requery.domain.upsert;

import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AbstractComponent;
import io.requery.Embedded;
import io.requery.Transient;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Diego on 2018. 6. 10..
 */
@Getter
@Embedded
public abstract class AbstractUpsertAddress extends AbstractComponent {

    protected String address;
    protected String city;
    protected String zipcode;

    @Override
    public int hashCode() {
        return zipcode.hashCode();
    }

    @NotNull
    @Override
    @Transient
    protected ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("zipcode", zipcode)
            .add("city", city)
            .add("address", address);
    }
}
