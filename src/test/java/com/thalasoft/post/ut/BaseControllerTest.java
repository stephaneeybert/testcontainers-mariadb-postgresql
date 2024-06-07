package com.thalasoft.post.ut;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public abstract class BaseControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    protected <T extends Object> T deserialize(final MvcResult mvcResult, Class<T> responseClass) throws Exception {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), responseClass);
    }

    protected <T extends Object> T deserializeResource(final MvcResult mvcResult, Class<T> responseClass)
            throws Exception {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), responseClass);
    }

    protected <T extends Object> ArrayList<T> deserializeResourcesArray(final MvcResult mvcResult,
            Class<T> responseClass) throws Exception {
        final CollectionType javaType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class,
                responseClass);
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), javaType);
    }

    protected <T extends Object> List<T> deserializeResourcesList(final MvcResult mvcResult,
            Class<T> responseClass) throws Exception {
        final CollectionType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class,
                responseClass);
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), javaType);
    }

}
