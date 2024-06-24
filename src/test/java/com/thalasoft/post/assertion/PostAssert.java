package com.thalasoft.post.assertion;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.AbstractAssert;

import com.thalasoft.post.model.PostModel;

public class PostAssert extends AbstractAssert<PostAssert, PostModel> {

    private PostAssert(PostModel actual) {
        super(actual, PostAssert.class);
    }

    public static PostAssert assertThatPost(PostModel actual) {
        return new PostAssert(actual);
    }

    public PostAssert hasId(Long id) {
        isNotNull();
        assertThat(actual.getId())
                .overridingErrorMessage("Expected the id to be <%s> but was <%s>.", id, actual.getId()).isEqualTo(id);
        return this;
    }

    public PostAssert hasUserId(Long userId) {
        isNotNull();
        assertThat(actual.getUserId())
                .overridingErrorMessage("Expected the userId to be <%s> but was <%s>.", userId, actual.getUserId())
                .isEqualTo(userId);
        return this;
    }

    public PostAssert hasTitle(String title) {
        isNotNull();
        assertThat(actual.getTitle())
                .overridingErrorMessage("Expected the title to be <%s> but was <%s>.", title, actual.getTitle())
                .isEqualTo(title.toString());
        return this;
    }

    public PostAssert hasBody() {
        isNotNull();
        assertThat(actual.getBody())
                .overridingErrorMessage("Expected the body to be not empty but it was.")
                .isNotEmpty();
        return this;
    }

    public PostAssert hasBody(String body) {
        isNotNull();
        assertThat(actual.getBody())
                .overridingErrorMessage("Expected the body to be <%s> but was <%s>.", body, actual.getBody())
                .isEqualTo(body.toString());
        return this;
    }

    public PostAssert hasIsbn(String isbn) {
        isNotNull();
        assertThat(actual.getIsbn())
                .overridingErrorMessage("Expected the ISBN to be <%s> but was <%s>.", isbn, actual.getIsbn())
                .isEqualTo(isbn.toString());
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
