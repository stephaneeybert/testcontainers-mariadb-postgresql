package com.thalasoft.post.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.MessageSource;

public class AbstractExceptionHandler {

    private final MessageSource messageSource;

    public AbstractExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    protected String localizeErrorMessage(String errorCode, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(errorCode, args, locale);
    }

    protected String localizeErrorMessage(String errorCode) {
        return localizeErrorMessage(errorCode, null);
    }

    protected String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }

}