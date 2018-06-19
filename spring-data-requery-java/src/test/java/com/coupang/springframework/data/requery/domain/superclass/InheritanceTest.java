package com.coupang.springframework.data.requery.domain.superclass;

import com.coupang.springframework.data.requery.domain.AbstractDomainTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Diego on 2018. 6. 13..
 */
public class InheritanceTest extends AbstractDomainTest {

    @Test
    public void create_derived_class() {

        RelatedEntity related = new RelatedEntity();
        related.setId(1L);
        requeryTemplate.insert(related);

        DerivedAEntity derivedA = new DerivedAEntity();
        derivedA.setId(2L);
        derivedA.getRelated().add(related);

        requeryTemplate.insert(derivedA);

        DerivedBEntity derivedB = new DerivedBEntity();
        derivedB.setId(3L);
        derivedB.getRelated().add(related);

        requeryTemplate.insert(derivedB);

        DerivedAEntity loadedA = requeryTemplate.findById(DerivedAEntity.class, 2L);
        assertThat(loadedA.getId()).isEqualTo(derivedA.getId());
        assertThat(loadedA.getRelated().iterator().next()).isEqualTo(related);

        DerivedBEntity loadedB = requeryTemplate.findById(DerivedBEntity.class, 3L);
        assertThat(loadedB.getId()).isEqualTo(derivedB.getId());
        assertThat(loadedB.getRelated().iterator().next()).isEqualTo(related);
    }
}
