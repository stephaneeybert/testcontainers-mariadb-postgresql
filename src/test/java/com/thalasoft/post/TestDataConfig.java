package com.thalasoft.post;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@Component
public class TestDataConfig {

    @Bean(destroyMethod = "stop")
    @ServiceConnection
    public MariaDBContainer<?> mariaDBContainer() {
        return new MariaDBContainer<>("mariadb:10.5.5")
                .withDatabaseName("posts")
                .withReuse(true);
    }

    @Bean(destroyMethod = "stop")
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:16.0")
                .withDatabaseName("posts")
                .withReuse(true);
    }
}
