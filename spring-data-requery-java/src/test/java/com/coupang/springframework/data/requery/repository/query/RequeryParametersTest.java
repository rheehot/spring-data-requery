package com.coupang.springframework.data.requery.repository.query;

import io.requery.query.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.data.repository.query.Param;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.coupang.springframework.data.requery.repository.query.RequeryParameters.RequeryParameter;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RequeryParametersTest {

    @Test
    public void findParameterConfiguration() throws Exception {

        Method method = SampleRepository.class.getMethod("foo", LocalDateTime.class, String.class);
        assertThat(method).isNotNull();

        RequeryParameters parameters = new RequeryParameters(method);

        RequeryParameter parameter = parameters.getBindableParameter(0);
        assertThat(parameter.isSpecialParameter()).isFalse();
        assertThat(parameter.isBindable()).isTrue();
        assertThat(parameter.isExplicitlyNamed()).isTrue();
        assertThat(parameter.getName().get()).isEqualTo("time");

        parameter = parameters.getBindableParameter(1);
        assertThat(parameter.isSpecialParameter()).isFalse();
        assertThat(parameter.isExplicitlyNamed()).isTrue();
        assertThat(parameter.isNamedParameter()).isTrue();
        assertThat(parameter.getName().get()).isEqualTo("firstname");
    }

    @Test
    public void findPrimitiveParameters() throws Exception {

        Method method = SampleRepository.class.getMethod("bar", int.class, String.class);
        assertThat(method).isNotNull();

        log.debug("return type={}", method.getReturnType());
        log.debug("return generic type={}", method.getGenericReturnType());


        RequeryParameters parameters = new RequeryParameters(method);

        RequeryParameter parameter = parameters.getBindableParameter(0);
        assertThat(parameter.isSpecialParameter()).isFalse();
        assertThat(parameter.isNamedParameter()).isFalse();
        assertThat(parameter.isExplicitlyNamed()).isFalse();

        parameter = parameters.getBindableParameter(1);
        assertThat(parameter.isSpecialParameter()).isFalse();
        assertThat(parameter.isNamedParameter()).isFalse();
        assertThat(parameter.isExplicitlyNamed()).isFalse();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void runInterfaceDefaultMethod() throws Exception {

        Method method = SampleRepository.class.getMethod("findByName", String.class);
        assertThat(method).isNotNull();
        assertThat(method.isDefault()).isTrue();

        // HINT: 이렇게 default method 에 대해서도 직접 실행이 가능하도록 할 수 있다.
        //
        SampleRepository repository = new SampleRepositoryImpl();
        Optional<String> result = (Optional<String>) method.invoke(repository, "value1");
        assertThat(result.get()).isEqualTo("value1");
    }

    interface SampleRepository {

        void foo(@Param("time") LocalDateTime localTime, @Param("firstname") String name);

        List<Tuple> bar(int age, String email);


        // 이렇게 default method 에 대해서도 직접 실행이 가능하도록 할 수 있다.
        default Optional<String> findByName(String name) {
            return Optional.ofNullable(name);
        }
    }

    class SampleRepositoryImpl implements SampleRepository {
        @Override
        public void foo(LocalDateTime localTime, String name) {
        }

        @Override
        public List<Tuple> bar(int age, String email) {
            return null;
        }
    }
}
