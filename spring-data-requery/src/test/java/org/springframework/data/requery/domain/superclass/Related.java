package org.springframework.data.requery.domain.superclass;

import io.requery.Entity;
import io.requery.Key;

/**
 * org.springframework.data.requery.domain.superclass.Related
 *
 * @author debop
 * @since 18. 6. 4
 */
@Entity
public interface Related {

    @Key
    Long getId();

}
