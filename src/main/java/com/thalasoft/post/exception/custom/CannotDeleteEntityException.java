package com.thalasoft.post.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotDeleteEntityException extends EnrichableException {

    public CannotDeleteEntityException() {
        super("The entity could not be deleted.");
    }

    public CannotDeleteEntityException(String message) {
        super(message);
    }

}