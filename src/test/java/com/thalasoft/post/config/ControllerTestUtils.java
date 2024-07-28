package com.thalasoft.post.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

@Component
public class ControllerTestUtils {

    @Autowired
    private ObjectMapper objectMapper;

    public <T extends Object> T deserialize(final MvcResult mvcResult, Class<T> responseClass) throws Exception {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), responseClass);
    }

    public <T extends Object> T deserializeResource(final MvcResult mvcResult, Class<T> responseClass)
            throws Exception {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), responseClass);
    }

    public <T extends Object> ArrayList<T> deserializeResourcesArray(final MvcResult mvcResult,
            Class<T> responseClass) throws Exception {
        final CollectionType javaType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class,
                responseClass);
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), javaType);
    }

    public <T extends Object> List<T> deserializeResourcesList(final MvcResult mvcResult,
            Class<T> responseClass) throws Exception {
        final CollectionType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class,
                responseClass);
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), javaType);
    }

}
