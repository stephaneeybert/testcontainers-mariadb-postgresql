package com.thalasoft.java.testcontainers.post;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
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

    @Test
    void shouldFindAll() {
        Post[] posts = restClient.get()
                .uri(API_ROOT)
                .retrieve()
                .body(Post[].class);

        assertThat(posts.length).isEqualTo(100);
    }

    @Test
    void shouldFindById() {
        Post post = restClient.get()
                .uri(API_ROOT + "/1")
                .retrieve()
                .body(Post.class);

        assertThat(post.body()).isNotEmpty();
    }

    @Test
    void shouldThrowNotFoundWhenInvalidPostID() {
        ResponseEntity<Void> responseEntity = restClient.get()
                .uri(API_ROOT + "/999")
                .retrieve()
                .toBodilessEntity();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        // Post result = restClient.get()
        // .uri("/api/posts/999")
        // .exchange((request, response) -> {
        // if (response.getStatusCode().is4xxClientError()) {
        // throw new NotFoundException("");
        // } else {
        // Post post = null;
        // return post;
        // }
        // }, false);
    }

    @Test
    @Rollback
    void shouldCreateNewPostWhenPostIsValid() {
        Post post = new Post(101, 1, "101 Title", "101 Body", null);

        ResponseEntity<Post> response = restClient.post()
                .uri(API_ROOT)
                .body(new HttpEntity<Post>(post))
                .retrieve()
                .toEntity(Post.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).id()).isEqualTo(101);
        assertThat(response.getBody().userId()).isEqualTo(1);
        assertThat(response.getBody().title()).isEqualTo("101 Title");
        assertThat(response.getBody().body()).isEqualTo("101 Body");
    }

    @Test
    void shouldNotCreateNewPostWhenValidationFails() {
        Post post = new Post(101, 1, "", "", null);
        ResponseEntity<Post> response = restClient.post()
                .uri(API_ROOT)
                .body(new HttpEntity<Post>(post))
                .retrieve()
                .toEntity(Post.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Rollback
    void shouldUpdatePostWhenPostIsValid() {
        Post existing = restClient.get()
                .uri(API_ROOT + "/99")
                .retrieve()
                .body(Post.class);

        assertThat(existing).isNotNull();

        Post touched = new Post(existing.id(), existing.userId(), "NEW POST TITLE #1", "NEW POST BODY #1",
                existing.version());
        Post updated = restClient.put()
                .uri(API_ROOT + "/99")
                .body(new HttpEntity<Post>(touched))
                .retrieve()
                .body(Post.class);

        assertThat(updated.id()).isEqualTo(99);
        assertThat(updated.userId()).isEqualTo(10);
        assertThat(updated.title()).isEqualTo("NEW POST TITLE #1");
        assertThat(updated.body()).isEqualTo("NEW POST BODY #1");
    }

    @Test
    @Rollback
    void shouldDeleteWithValidID() {
        ResponseEntity<Void> response = restClient.delete()
                .uri(API_ROOT + "/88")
                .retrieve()
                .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
