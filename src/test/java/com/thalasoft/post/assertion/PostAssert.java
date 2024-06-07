package com.thalasoft.post.assertion;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.AbstractAssert;

import com.thalasoft.post.Post;

public class PostAssert extends AbstractAssert<PostAssert, Post> {

    private PostAssert(Post actual) {
        super(actual, PostAssert.class);
    }

    public static PostAssert assertThatPost(Post actual) {
        return new PostAssert(actual);
    }

    public PostAssert hasId(Integer id) {
        isNotNull();
        assertThat(actual.id())
                .overridingErrorMessage("Expected the id to be <%s> but was <%s>.", id, actual.id()).isEqualTo(id);
        return this;
    }

    public PostAssert hasUserId(Integer userId) {
        isNotNull();
        assertThat(actual.userId())
                .overridingErrorMessage("Expected the userId to be <%s> but was <%s>.", userId, actual.userId())
                .isEqualTo(userId);
        return this;
    }

    public PostAssert hasTitle(String title) {
        isNotNull();
        assertThat(actual.title())
                .overridingErrorMessage("Expected the title to be <%s> but was <%s>.", title, actual.title())
                .isEqualTo(title.toString());
        return this;
    }

    public PostAssert hasBody() {
        isNotNull();
        assertThat(actual.body())
                .overridingErrorMessage("Expected the body to be not empty but it was.")
                .isNotEmpty();
        return this;
    }

    public PostAssert hasBody(String body) {
        isNotNull();
        assertThat(actual.body())
                .overridingErrorMessage("Expected the body to be <%s> but was <%s>.", body, actual.body())
                .isEqualTo(body.toString());
        return this;
    }

    public PostAssert exists() {
        assertThat(actual).overridingErrorMessage("Expected the post to exist but it didn't.").isNotNull();
        return this;
    }

    public PostAssert doesNotExist() {
        assertThat(actual).overridingErrorMessage("Expected the post not to exist but it did.").isNull();
        return this;
    }
}
