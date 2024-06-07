package com.thalasoft.post.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.thalasoft.post.BaseControllerTest;
import com.thalasoft.post.Post;
import com.thalasoft.post.TestDataConfig;
import com.thalasoft.post.TestContainersApplication;

@Testcontainers
@Import(TestDataConfig.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = { TestContainersApplication.class })
class PostControllerTest extends BaseControllerTest {

    private static final String API_ROOT = "/api/posts";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldFindAll() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(API_ROOT);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        ArrayList<Post> posts = deserializeResourcesArray(mvcResult, Post.class);
        assertThat(posts).hasSize(100);
    }
}
