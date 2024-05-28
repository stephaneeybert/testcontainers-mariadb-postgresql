package com.thalasoft.java.testcontainers.post;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostControllerTest {

    @Autowired
    MariaDBContainer<?> mariaDBContainer;

    @Autowired
    PostgreSQLContainer<?> postgreSQLContainer;

    @Autowired
    PostRepository postRepository;

    @Autowired
    RestClient restClient;

    @Test
    void shouldFindAll() {
        Post[] posts = restClient.get()
        .uri("/api/posts")
        .retrieve()
        .body(Post[].class);
        assertThat(posts.length).isEqualTo(100);
    }
}
