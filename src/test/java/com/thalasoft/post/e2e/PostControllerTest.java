package com.thalasoft.post.e2e;

import static com.thalasoft.post.assertion.PostAssert.assertThatPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

import com.thalasoft.post.TestDataConfig;
import com.thalasoft.post.entity.Post;
import com.thalasoft.post.model.PostModel;
import com.thalasoft.post.service.ModelService;
import com.thalasoft.post.utils.RESTUtils;

import jakarta.persistence.EntityNotFoundException;

@Testcontainers
@Import(TestDataConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostControllerTest {

    private static final String API_ROOT = RESTUtils.SLASH + RESTUtils.API + RESTUtils.SLASH + RESTUtils.POSTS;
    private static final long EXISTING_ID = 1;
    private static final long NON_EXISTING_ID = 999;

    @Autowired
    private RestClient restClient;

    @Autowired
    private ModelService modelService;

    @LocalServerPort
    private int port;
    private String uriBase;

    @BeforeEach
    public void setup() {
        uriBase = "http://localhost:" + port;
    }

    @Test
    void shouldFindAll() {
        PostModel[] postModels = restClient.get()
                .uri(uriBase + API_ROOT)
                .retrieve()
                .body(PostModel[].class);

        assertThat(postModels.length).isEqualTo(100);
    }

    @Test
    void shouldFindById() {
        PostModel postModel = restClient.get()
                .uri(uriBase + API_ROOT + "/" + EXISTING_ID)
                .retrieve()
                .body(PostModel.class);

        assertThatPost(postModel).hasId(1L).hasBody();
    }

    @Test
    void shouldThrowNotFoundWhenInvalidId() {
        assertThatThrownBy(() -> {
            restClient.get()
                    .uri(uriBase + API_ROOT + "/" + NON_EXISTING_ID)
                    .retrieve()
                    .toBodilessEntity();
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldThrowNotFoundWhenFindingInvalidId() {
        assertThatThrownBy(() -> {
            restClient.get()
                    .uri(uriBase + API_ROOT + "/find/" + NON_EXISTING_ID)
                    .retrieve()
                    .toBodilessEntity();
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldCreateNewWhenModelIsValid() {
        long userId = 1;
        String title = "The title";
        String body = "The body";
        String isbn = "1";
        Post post = Post.builder().userId(userId).title(title).body(body).isbn(isbn).build();

        ResponseEntity<PostModel> response = restClient.post()
                .uri(uriBase + API_ROOT)
                .body(post)
                .retrieve()
                .toEntity(PostModel.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        PostModel posted = response.getBody();
        assertThatPost(posted).exists().hasUserId(userId).hasTitle(title).hasBody(body).hasIsbn(isbn);

        restClient.delete()
                .uri(uriBase + API_ROOT + "/" + posted.getId())
                .retrieve()
                .toBodilessEntity();
    }

    @Test
    void shouldNotCreateNewWhenModelValidationFails() {
        Post post = Post.builder().userId(1L).title("").body("").isbn("").build();
        PostModel postModel = modelService.fromPost(post);
        assertThatThrownBy(() -> {
            ResponseEntity<PostModel> response = restClient.post()
                    .uri(uriBase + API_ROOT)
                    .body(postModel)
                    .retrieve()
                    .toEntity(PostModel.class);
        }).isInstanceOf(HttpClientErrorException.class);
    }

    @Test
    void shouldUpdateWhenModelIsValid() {
        PostModel existing = restClient.get()
                .uri(uriBase + API_ROOT + "/" + EXISTING_ID)
                .retrieve()
                .body(PostModel.class);

        assertThat(existing).isNotNull();

        String title = "NEW POST TITLE #1";
        String body = "NEW POST BODY #1";
        String isbn = "3213423";
        Post touched = Post.builder().id(existing.getId()).userId(existing.getUserId()).title(title).body(body)
                .isbn(isbn)
                .build();

        PostModel updated = restClient.put()
                .uri(uriBase + API_ROOT + "/" + existing.getId())
                .body(touched)
                .retrieve()
                .body(PostModel.class);

        assertThatPost(updated).hasId(existing.getId()).hasUserId(existing.getUserId()).hasTitle(title).hasBody(body);
    }

    @Test
    void shouldNotUpdateWhenValidationFails() {
    }

    @Test
    void shouldNotUpdateWhenDoesNotYetExist() {
    }

    @Test
    void shouldDeleteOnValidId() {
        ResponseEntity<Void> response = restClient.delete()
                .uri(uriBase + API_ROOT + "/" + EXISTING_ID)
                .retrieve()
                .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test // given when then TODO
    void shouldNotDeleteOnInvalidId() {
    }
}
