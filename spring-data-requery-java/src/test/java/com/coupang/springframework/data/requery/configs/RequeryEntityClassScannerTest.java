package com.coupang.springframework.data.requery.configs;

import com.coupang.springframework.data.requery.configs.RequeryEntityClassScanner;
import com.coupang.springframework.data.requery.domain.basic.AbstractBasicUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * com.coupang.springframework.data.requery.configs.RequeryEntityClassScannerTest
 *
 * @author debop
 * @since 18. 6. 7
 */
@Slf4j
public class RequeryEntityClassScannerTest {

    @Test
    public void scanRequeryEntities() throws Exception {
        Package pkg = AbstractBasicUser.class.getPackage();
        Set<Class<?>> entityClasses = RequeryEntityClassScanner.scanForEntities(pkg.getName());

        for (Class<?> entityClass : entityClasses) {
            log.debug("EntityClass={}", entityClass.getName());

        }
        assertThat(entityClasses).isNotEmpty();
    }
}
