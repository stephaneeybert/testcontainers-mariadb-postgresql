package com.thalasoft.java.testcontainers.post;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import jakarta.validation.constraints.NotEmpty;

public record Post(
        @Id
        Integer id,
        Integer userId,
        @NotEmpty
        String title,
        @NotEmpty
        String body,
        @Version
        Integer version
        ) {
}
