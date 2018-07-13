package org.springframework.boot.autoconfigure.data.requery;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.autoconfigure.data.requery.domain.Models;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * org.springframework.boot.autoconfigure.data.requery.RequeryPropertiesTest
 *
 * @author debop
 */
@Slf4j
public class RequeryPropertiesTest {

    @Test
    public void instancingEntityModel() throws Exception {

        String modelFullName = Models.class.getName() + ".DEFAULT";
        String className = StringUtils.stripFilenameExtension(modelFullName);
        String modelName = StringUtils.getFilenameExtension(modelFullName);

        Class<?> clazz = Class.forName(className);

        Field field = clazz.getField(modelName);

        Object value = field.get(null);

        log.debug("model={}", value);

        assertThat(value).isEqualTo(Models.DEFAULT);
    }
}
