package com.coupang.springframework.data.requery.repository.config;

import com.coupang.springframework.data.requery.repository.config.InspectionClassLoader;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * InspectionClassLoaderTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
public class InspectionClassLoaderTest {

    @Test
    public void shouldLoadExternalClass() throws ClassNotFoundException {

        InspectionClassLoader classLoader = new InspectionClassLoader(getClass().getClassLoader());

        Class<?> isolated = classLoader.loadClass("org.h2.Driver");
        Class<?> included = getClass().getClassLoader().loadClass("org.h2.Driver");

        assertThat(isolated.getClassLoader())
            .isSameAs(classLoader)
            .isNotSameAs(getClass().getClassLoader());

        assertThat(isolated).isNotEqualTo(included);
    }
}
