package com.dennis.jdbc.helper.exception;

public class NoColumnAnnotationException extends RuntimeException {
    public NoColumnAnnotationException() {
    }

    public NoColumnAnnotationException(String msg) {
        super(msg);
    }
}
