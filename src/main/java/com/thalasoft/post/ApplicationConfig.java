package com.thalasoft.post;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ApplicationConfig {

    @Bean
    RestClient restClient() {
        return RestClient.create();
    }
}
