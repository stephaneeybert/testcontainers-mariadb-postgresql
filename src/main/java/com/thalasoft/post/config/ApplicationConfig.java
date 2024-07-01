package com.thalasoft.post.config;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ApplicationConfig {

    @Bean
    RestClient restClient() {
        return RestClient.create();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages/messages", "classpath:messages/validation");
        // If true, the key of the message will be displayed if the key is not
        // found, instead of throwing an exception
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding(UTF_8.name());
        // The value 0 means always reload the messages to be developer friendly
        messageSource.setCacheSeconds(0);
        return messageSource;
    }
}
