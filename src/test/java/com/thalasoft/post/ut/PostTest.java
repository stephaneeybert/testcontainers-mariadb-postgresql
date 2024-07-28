package com.thalasoft.post.ut;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thalasoft.post.entity.Post;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class PostTest {

    private Validator validator;

    private Post post;
    private Set<ConstraintViolation<Post>> constraintViolations;

    @BeforeEach
    public void beforeEachTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        post = Post.builder().userId(1L).title("My title").body("Some body").isbn("32132").build();
    }

    @Test
    void testNoValidationViolation() {
        constraintViolations = validator.validate(post);
        assertEquals(0, constraintViolations.size());
    }

}
