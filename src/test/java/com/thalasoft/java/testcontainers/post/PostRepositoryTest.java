package com.thalasoft.java.testcontainers.post;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.thalasoft.java.testcontainers.TestContainerConfig;

@Testcontainers
@DataJdbcTest
@Import(TestContainerConfig.class)
class PostRepositoryTest {

    @Autowired
    MariaDBContainer<?> mariaDBContainer;

    @Autowired
    PostgreSQLContainer<?> postgreSQLContainer;

    @Autowired
    PostRepository postRepository;

    @Test
    void mariadbConnectionEstablished() {
        assertThat(mariaDBContainer.isCreated()).isTrue();
        assertThat(mariaDBContainer.isRunning()).isTrue();
    }

    @Test
    void postgresqlConnectionEstablished() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @BeforeEach
    void setup() {
        List<Post> posts = List.of(new Post(1, 1, "Hi", "The hi post", null));
        postRepository.saveAll(posts);
    }

    @Test
    void shouldReturnPostByTitle() {
        Post post = postRepository.findByTitle("Hi");
        assertThat(post).isNotNull();
    }
}
