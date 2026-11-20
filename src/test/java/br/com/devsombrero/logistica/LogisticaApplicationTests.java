package br.com.devsombrero.logistica;

import br.com.devsombrero.logistica.support.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class LogisticaApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertThat(context).isNotNull();
    }

    @Test
    void mainDeveDelegarParaSpringApplication() {
        String[] args = {"--qualquer=valor"};

        try (MockedStatic<SpringApplication> spring = mockStatic(SpringApplication.class)) {
            LogisticaApplication.main(args);

            spring.verify(() -> SpringApplication.run(LogisticaApplication.class, args));
        }
    }
}
