package com.thalasoft.java.testcontainers.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.thalasoft.java.testcontainers.TestContainerConfig;

@Testcontainers
@Import(TestContainerConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostControllerTest {

    private static final String API_ROOT = "/api/posts";

    @Autowired
    MariaDBContainer<?> mariaDBContainer;

    @Autowired
    PostgreSQLContainer<?> postgreSQLContainer;

    @Autowired
    PostRepository postRepository;

    @Autowired
    RestClient restClient;

    @LocalServerPort
    private int port;
    private String uriBase;

    @BeforeEach
    public void setup() {
        uriBase = "http://localhost:" + port;
    }

    @Test
    void shouldFindAll() {
        Post[] posts = restClient.get()
                .uri(uriBase + API_ROOT)
                .retrieve()
                .body(Post[].class);

        assertThat(posts.length).isEqualTo(100);
    }

    @Test
    void shouldFindById() {
        Post post = restClient.get()
                .uri(uriBase + API_ROOT + "/1")
                .retrieve()
                .body(Post.class);

        assertThat(post.body()).isNotEmpty();
    }

    @Test
    void shouldThrowNotFoundWhenInvalidPostID() {
        assertThatThrownBy(() -> {
            ResponseEntity<Void> responseEntity = restClient.get()
                    .uri(uriBase + API_ROOT + "/999")
                    .retrieve()
                    .toBodilessEntity();

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }).isInstanceOf(HttpClientErrorException.class);
    }

    @Test
    void shouldCreateNewPostWhenPostIsValid() {
        Post post = new Post(101, 1, "101 Title", "101 Body", null);

        ResponseEntity<Post> response = restClient.post()
                .uri(uriBase + API_ROOT)
                .body(post)
                .retrieve()
                .toEntity(Post.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).id()).isEqualTo(101);
        assertThat(response.getBody().userId()).isEqualTo(1);
        assertThat(response.getBody().title()).isEqualTo("101 Title");
        assertThat(response.getBody().body()).isEqualTo("101 Body");

        ResponseEntity<Void> deleted = restClient.delete()
        .uri(uriBase + API_ROOT + "/101")
        .retrieve()
        .toBodilessEntity();
    }

    @Test
    void shouldNotCreateNewPostWhenValidationFails() {
        Post post = new Post(101, 1, "", "", null);
        assertThatThrownBy(() -> {
            ResponseEntity<Post> response = restClient.post()
                    .uri(uriBase + API_ROOT)
                    .body(post)
                    .retrieve()
                    .toEntity(Post.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }).isInstanceOf(HttpClientErrorException.class);
    }

    @Test
    void shouldUpdatePostWhenPostIsValid() {
        Post existing = restClient.get()
                .uri(uriBase + API_ROOT + "/91")
                .retrieve()
                .body(Post.class);

        assertThat(existing).isNotNull();

        Post touched = new Post(existing.id(), existing.userId(), "NEW POST TITLE #1", "NEW POST BODY #1",
                existing.version());

        Post updated = restClient.put()
                .uri(uriBase + API_ROOT + "/" + existing.id())
                .body(touched)
                .retrieve()
                .body(Post.class);

        assertThat(updated.id()).isEqualTo(existing.id());
        assertThat(updated.userId()).isEqualTo(existing.userId());
        assertThat(updated.title()).isEqualTo("NEW POST TITLE #1");
        assertThat(updated.body()).isEqualTo("NEW POST BODY #1");
    }

    @Test
    void shouldDeleteWithValidID() {
        ResponseEntity<Void> response = restClient.delete()
                .uri(uriBase + API_ROOT + "/88")
                .retrieve()
                .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
