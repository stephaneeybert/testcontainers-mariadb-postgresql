package com.thalasoft.post.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import com.thalasoft.post.PostRepository;
import com.thalasoft.post.entity.Post;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    private MariaDBContainer<?> mariaDBContainer;

    @Autowired
    private PostgreSQLContainer<?> postgreSQLContainer;

    @Autowired
    private PostRepository postRepository;

    private List<Post> posts;

    @BeforeEach
    void setup() {
        posts = List.of(
                Post.builder().userId(1L).title("Hi").body("The hi post").isbn("32432").build());
        postRepository.saveAll(posts);
    }

    @AfterEach
    void cleanup() {
        posts.stream().map(p -> p.getId()).forEach(id -> postRepository.deleteById(id));
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
