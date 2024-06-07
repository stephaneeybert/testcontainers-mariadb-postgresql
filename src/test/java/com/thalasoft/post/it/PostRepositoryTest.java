package com.thalasoft.post.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thalasoft.post.Post;
import com.thalasoft.post.BaseDataTest;

class PostRepositoryTest extends BaseDataTest {

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
