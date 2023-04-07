package ru.tinkoff.edu.java.scrapper.enviroment;


import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Testcontainers
public abstract class IntegrationEnvironment {
    protected static final PostgreSQLContainer<?> PSQL_CONTAINER;

    private static final String CHANGELOG_FILE = "master.xml";
    private static final Path PATH_TO_CHANGELOG = new File("migrations/changelogs").toPath();

    static {
        PSQL_CONTAINER = new PostgreSQLContainer<>("postgres:15");
        PSQL_CONTAINER.start();
        executeDatabaseMigrations();
    }

    private static void executeDatabaseMigrations() {
        try (Connection connection = DriverManager.getConnection(
                PSQL_CONTAINER.getJdbcUrl(),
                PSQL_CONTAINER.getUsername(),
                PSQL_CONTAINER.getPassword()
        )) {
            ResourceAccessor resourceAccessor = new DirectoryResourceAccessor(PATH_TO_CHANGELOG);
            Database postgres = new PostgresDatabase();
            postgres.setConnection(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(CHANGELOG_FILE, resourceAccessor, postgres);
            liquibase.update();

        } catch (SQLException | FileNotFoundException | LiquibaseException e) {
            log.error("database migration failed");
            throw new RuntimeException(e);
        }
    }
}
