package com.thalasoft.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

import com.thalasoft.post.entity.Post;
import com.thalasoft.post.exception.custom.EntityAlreadyExistsException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@SpringBootTest
class PostServiceTest {

    private static final long EXISTING_ID = 1;
    private static final long NON_EXISTING_ID = 999;
    private static final String NON_EXISTING_ISBN = "AZ999";

    @Autowired
    private PostService postService;

    // The getReferenceById repository method is lazy loading
    // and not accessing the entity inside of the transaction scope
    // and accessing the entity outside of the transaction scope
    // should trigger a LazyInitializationException exception
    @Test
    void whenGetUsedOutsideOfServiceThenThrow() {
        Post post = postService.getReferenceById(EXISTING_ID);
        assertThatExceptionOfType(LazyInitializationException.class)
                .isThrownBy(post::getTitle);
    }

    // The findById repository method is eager loading
    // with the entity loaded even if not being accessed at all
    @Test
    void whenFindOutsideOfServiceThenDoNotThrow() {
        Post post = postService.findById(EXISTING_ID);
        assertThat(post.getId()).isEqualTo(EXISTING_ID);
        assertThat(post.getTitle()).isNotEmpty();
    }

    @Test
    void whenFindNonExistingIdThenThrow() {
        assertThatThrownBy(() -> {
            postService.findById(NON_EXISTING_ID);
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void whenFindByIsbnNonExistingThenThrow() {
        assertThatThrownBy(() -> {
            postService.findByIsbn(NON_EXISTING_ISBN);
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void whenFindAllByIdsThenReturnAll() {
        List<Long> list = Arrays.asList(new Long[] { 1L, 2L, 3L });
        List<Post> posts = postService.findAllByIds(list);
        assertThat(posts).hasSize(3);
    }

    @Test
    void whenFindAllThenReturnAll() {
        List<Post> posts = postService.findAll();
        assertThat(posts).hasSizeGreaterThanOrEqualTo(100);
    }

    @Test
    void whenAddValidModelThenReturnAddedModel() {
        long userId = 1;
        String title = "The title";
        String body = "The body";
        String isbn = "1";
        Post post = Post.builder().userId(userId).title(title).body(body).isbn(isbn).build();
        post = postService.add(post);
        assertThat(post.getId()).isPositive();
        assertThat(post.getTitle()).isNotEmpty();
    }

    @Test
    void whenAddInvalidEntityThenThrow() {
        Post post0 = Post.builder().userId(1L).title("").body("Some").isbn("328921").build();
        assertThatThrownBy(() -> {
            postService.add(post0);
        }).isInstanceOf(ConstraintViolationException.class);
        Post post1 = Post.builder().userId(1L).title("Some").body("").isbn("32957321").build();
        assertThatThrownBy(() -> {
            postService.add(post1);
        }).isInstanceOf(ConstraintViolationException.class);
        Post post2 = Post.builder().userId(1L).title("Some").body("Some").isbn("").build();
        assertThatThrownBy(() -> {
            postService.add(post2);
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void givenEntityWhenAddingModelWithSameEntityIdThenThrow() {
        Post post = Post.builder().id(NON_EXISTING_ID).version(1).userId(1L).title("Some").body("Some").isbn("325421").build();
        Post posted = postService.add(post);
        assertThatExceptionOfType(EntityAlreadyExistsException.class).isThrownBy(() -> postService.add(post));
        postService.delete(posted.getId());
    }

    @Test
    void givenEntityWhenAddingModelWithSameIsbnThenThrow() {
        String isbn = "325421";
        Post post0 = postService.add(Post.builder().userId(1L).title("Some").body("Some").isbn(isbn).build());
        Post post1 = Post.builder().userId(1L).title("Some too").body("Some too").isbn(isbn).build();
        assertThatExceptionOfType(EntityAlreadyExistsException.class).isThrownBy(() -> postService.add(post1));
        postService.delete(post0.getId());
    }

    @Test
    void whenUpdateValidEntityThenReturnAddedEntity() {
        Post post = Post.builder().userId(1L).title("Some").body("Some").isbn("4563644").build();
        post = postService.add(post);
        assertThat(post).isNotNull();

        long userId = 2;
        String title = "The updated title";
        String body = "The updated body";
        String isbn = "UPDATED1";
        post.setUserId(userId);
        post.setTitle(title);
        post.setBody(body);
        post.setIsbn(isbn);
        post = postService.update(post.getId(), post);
        assertThat(post).isNotNull();
        assertThat(post.getUserId()).isEqualTo(userId);
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getBody()).isEqualTo(body);
        assertThat(post.getIsbn()).isEqualTo(isbn);
    }

    @Test
    void whenUpdateInvalidEntityThenThrow() {
        Post post = Post.builder().userId(1L).title("Some").body("Some").isbn("321321").build();
        Post added = postService.add(post);
        assertThat(added.getId()).isPositive();

        added.setTitle("");
        assertThatThrownBy(() -> {
            postService.update(added.getId(), added);
            fail();
        })
        .isInstanceOf(TransactionSystemException.class)
        .hasRootCauseInstanceOf(ConstraintViolationException.class)
        .rootCause()
        .hasMessageContaining("must not be empty");
        added.setTitle("Some");
        added.setBody("");
        assertThatThrownBy(() -> {
            postService.update(added.getId(), added);
            fail();
        })
        .isInstanceOf(TransactionSystemException.class)
        .hasRootCauseInstanceOf(ConstraintViolationException.class)
        .rootCause()
        .hasMessageContaining("must not be empty");
        added.setBody("Some");
        added.setIsbn("");
        assertThatThrownBy(() -> {
            postService.update(added.getId(), added);
            fail();
        })
        .isInstanceOf(TransactionSystemException.class)
        .hasRootCauseInstanceOf(ConstraintViolationException.class)
        .rootCause()
        .hasMessageContaining("must not be empty");
    }
}
