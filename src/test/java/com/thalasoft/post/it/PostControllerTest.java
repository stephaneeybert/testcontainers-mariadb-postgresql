package com.thalasoft.post.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.thalasoft.post.model.PostModel;
import com.thalasoft.post.utils.RESTUtils;
import com.thalasoft.post.config.ControllerTestUtils;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    private static final String API_ROOT = RESTUtils.SLASH + RESTUtils.API + RESTUtils.SLASH + RESTUtils.POSTS;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ControllerTestUtils controllerTestUtils;

    @Test
    void shouldFindAll() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(API_ROOT);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        ArrayList<PostModel> postModels = controllerTestUtils.deserializeResourcesArray(mvcResult, PostModel.class);
        assertThat(postModels).hasSize(100);
    }
}
