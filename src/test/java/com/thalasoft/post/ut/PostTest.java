package com.thalasoft.post.ut;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thalasoft.post.Post;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class PostTest {

    private Validator validator;

    private Post post;
    private Set<ConstraintViolation<Post>> constraintViolations;

    @BeforeEach
    public void beforeAnyTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        post = new Post(1, 1, "My title", "Some body", 1);
    }

    @Test
    void testNoValidationViolation() {
        constraintViolations = validator.validate(post);
        assertEquals(0, constraintViolations.size());
    }

}
