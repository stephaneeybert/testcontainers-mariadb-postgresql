package com.thalasoft.post.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityAlreadyExistsException extends EnrichableException {

    public EntityAlreadyExistsException() {
        super("The entity already exists.");
    }

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

}