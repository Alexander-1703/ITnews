package ru.tinkoff.edu.java.jooq;


import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Target;

public class JooqCodegen extends IntegrationEnvironment {
    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration()
                .withJdbc(new Jdbc()
                        .withDriver(PSQL_CONTAINER.getDriverClassName())
                        .withUrl(PSQL_CONTAINER.getJdbcUrl())
                        .withUser(PSQL_CONTAINER.getUsername())
                        .withPassword(PSQL_CONTAINER.getPassword()))
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withName("org.jooq.meta.postgres.PostgresDatabase")
                                .withInputSchema("public"))
                        .withGenerate(new Generate()
                                .withGeneratedAnnotation(true)
                                .withGeneratedAnnotationDate(false)
                                .withNullableAnnotation(true)
                                .withNullableAnnotationType("org.jetbrains.annotations.Nullable")
                                .withNonnullAnnotation(true)
                                .withNonnullAnnotationType("org.jetbrains.annotations.NotNull")
                                .withJpaAnnotations(false)
                                .withValidationAnnotations(true)
                                .withSpringAnnotations(true)
                                .withConstructorPropertiesAnnotation(true)
                                .withConstructorPropertiesAnnotationOnPojos(true)
                                .withConstructorPropertiesAnnotationOnRecords(true)
                                .withFluentSetters(false)
                                .withDaos(false)
                                .withPojos(true))
                        .withTarget(new Target()
                                .withPackageName("ru.tinkoff.edu.java.scrapper.domain.jooq")
                                .withDirectory("scrapper/src/main/java")));

        GenerationTool.generate(configuration);
    }
}
