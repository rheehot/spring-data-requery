package org.springframework.data.requery.domain.model;

import io.requery.Column;
import io.requery.Lazy;
import io.requery.Superclass;

/**
 * com.coupang.springframework.data.requery.domain.model.Coordinate
 *
 * @author debop
 * @since 18. 6. 4
 */
@Superclass
public class Coordinate {

    @Lazy
    @Column(value = "0.0", nullable = false)
    protected float latitude;

    @Lazy
    @Column(value = "0.0", nullable = false)
    protected float longitude;
}
