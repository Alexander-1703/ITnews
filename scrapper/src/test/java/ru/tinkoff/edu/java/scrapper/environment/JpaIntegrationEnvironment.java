package ru.tinkoff.edu.java.scrapper.environment;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;

@SpringBootTest(classes = IntegrationEnvironment.TestDataSourceConfiguration.class)
public class JpaIntegrationEnvironment {
    @DynamicPropertySource
    static void jpaProperties(DynamicPropertyRegistry registry) {
        registry.add("scrapper.accessType", () -> ApplicationConfig.AccessType.JPA);
    }
}
