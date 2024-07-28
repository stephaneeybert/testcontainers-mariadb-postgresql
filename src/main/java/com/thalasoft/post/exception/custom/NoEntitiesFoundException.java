package com.thalasoft.post.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoEntitiesFoundException extends EnrichableException {

    public NoEntitiesFoundException() {
        super("No entities were found.");
    }

    public NoEntitiesFoundException(String message) {
        super(message);
    }

}
