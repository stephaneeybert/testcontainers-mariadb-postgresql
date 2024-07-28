package com.thalasoft.post.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@Component
public class TestDataConfig {

    private static final String MARIADB_IMAGE_VERSION = "mariadb:10.5.5";
    private static final String POSTGRES_IMAGE_VERSION = "postgres:16.0";
    private static final String DATABASE_NAME = "posts";

    @Bean(destroyMethod = "stop")
    @ServiceConnection
    public MariaDBContainer<?> mariaDBContainer() {
        return new MariaDBContainer<>(MARIADB_IMAGE_VERSION)
                .withDatabaseName(DATABASE_NAME)
                .withReuse(true);
    }

    @Bean(destroyMethod = "stop")
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>(POSTGRES_IMAGE_VERSION)
                .withDatabaseName(DATABASE_NAME)
                .withReuse(true);
    }
}
