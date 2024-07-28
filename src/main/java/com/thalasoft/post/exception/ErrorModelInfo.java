package com.thalasoft.post.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

public class ErrorModelInfo {

    private String url;
    private HttpStatus httpStatus;
    private String message;
    private List<ErrorModelField> fieldErrors = new ArrayList<>();

    public ErrorModelInfo() {
    }

    public ErrorModelInfo(String url, HttpStatus httpStatus, String message) {
        this.url = url;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public ErrorModelInfo(String url, HttpStatus httpStatus, String message, List<ErrorModelField> fieldErrors) {
        this.url = url;
        this.httpStatus = httpStatus;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ErrorModelField> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<ErrorModelField> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}