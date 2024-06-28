package com.thalasoft.post.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thalasoft.post.BaseDataTest;
import com.thalasoft.post.entity.Post;

class PostRepositoryTest extends BaseDataTest {

    @BeforeEach
    void setup() {
        List<Post> posts = List.of(
                Post.builder().userId(1L).title("Hi").body("The hi post").isbn("32432").build());
        postRepository.saveAll(posts);
    }

    @AfterEach
    void cleanup() {
        postRepository.deleteAll();
    }

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

    @Test
    void shouldReturnPostByTitle() {
        Optional<Post> post = postRepository.findByTitle("Hi");
        assertThat(post).isPresent();
    }
}
