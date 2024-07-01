package com.thalasoft.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.thalasoft.post.config.TestDataConfig;

@Testcontainers
@SpringBootTest
@Import(TestDataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration
public abstract class BaseDataTest {

    @Autowired
    protected MariaDBContainer<?> mariaDBContainer;

    @Autowired
    protected PostgreSQLContainer<?> postgreSQLContainer;

    @Autowired
    protected PostRepository postRepository;

}
