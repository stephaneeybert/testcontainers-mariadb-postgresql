package com.thalasoft.post.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static com.thalasoft.post.assertion.PostAssert.assertThatPost;

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
import org.testcontainers.junit.jupiter.Testcontainers;

import com.thalasoft.post.Post;
import com.thalasoft.post.TestDataConfig;

@Testcontainers
@Import(TestDataConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostControllerTest {

    private static final String API_ROOT = "/api/posts";

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

        assertThatPost(post).hasId(1).hasBody();
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
        int id = 101;
        String title = "The title";
        String body = "The body";
        int userId = 1;
        Post post = new Post(id, userId, title, body, null);

        ResponseEntity<Post> response = restClient.post()
                .uri(uriBase + API_ROOT)
                .body(post)
                .retrieve()
                .toEntity(Post.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        Post posted = response.getBody();
        assertThatPost(posted).exists().hasId(id).hasUserId(userId).hasTitle(title).hasBody(body);

        ResponseEntity<Void> deleted = restClient.delete()
                .uri(uriBase + API_ROOT + "/" + id)
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

        String title = "NEW POST TITLE #1";
        String body = "NEW POST BODY #1";
        Post touched = new Post(existing.id(), existing.userId(), title, body,
                existing.version());

        Post updated = restClient.put()
                .uri(uriBase + API_ROOT + "/" + existing.id())
                .body(touched)
                .retrieve()
                .body(Post.class);

        assertThatPost(updated).hasId(existing.id()).hasUserId(existing.userId()).hasTitle(title).hasBody(body);
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
