package org.springframework.data.requery.domain.sample;

import io.requery.Entity;
import io.requery.Table;

/**
 * org.springframework.data.requery.domain.sample.AbstractSpecialUser
 *
 * @author debop
 * @since 18. 6. 14
 */
// NOTE: @Superclass 가 아닌 @Entity로부터 상속받은 entity 는 잘못 될 수 있다.
@Entity
@Table(name = "SpectialUser")
public abstract class AbstractSpecialUser extends User {


    protected String specialAttribute;

    private static final long serialVersionUID = -7890331277430318674L;
}
