package ru.tinkoff.edu.java.scrapper.configuration.database;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "scrapper", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

}
