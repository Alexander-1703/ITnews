package ru.tinkoff.edu.java.jooq;


import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.testcontainers.containers.PostgreSQLContainer;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;

public abstract class IntegrationEnvironment {
    protected static final PostgreSQLContainer<?> PSQL_CONTAINER;

    private static final String CHANGELOG_FILE = "master.xml";
    private static final Path PATH_TO_CHANGELOG = new File("scrapper/migrations/changelogs").toPath().toAbsolutePath();

    static {
        PSQL_CONTAINER = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName("scrapper")
                .withUsername("postgres")
                .withPassword("postgres");
        PSQL_CONTAINER.start();
        executeDatabaseMigrations();
    }

    private static void executeDatabaseMigrations() {
        try (Connection connection = DriverManager.getConnection(
                PSQL_CONTAINER.getJdbcUrl(),
                PSQL_CONTAINER.getUsername(),
                PSQL_CONTAINER.getPassword()
        )) {
            System.out.println("PATH_TO_CHANGELOG: " + PATH_TO_CHANGELOG);

            ResourceAccessor resourceAccessor = new DirectoryResourceAccessor(PATH_TO_CHANGELOG);
            Database postgres = new PostgresDatabase();
            postgres.setConnection(new JdbcConnection(connection));

            var liquibase = new Liquibase(CHANGELOG_FILE, resourceAccessor, postgres);
            liquibase.update();

        } catch (SQLException | FileNotFoundException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
