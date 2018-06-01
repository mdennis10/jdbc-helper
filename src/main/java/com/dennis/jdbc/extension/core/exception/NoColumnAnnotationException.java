package com.dennis.jdbc.extension.core.exception;

public class NoColumnAnnotationException extends RuntimeException {
    public NoColumnAnnotationException() {
    }

    public NoColumnAnnotationException(String msg) {
        super(msg);
    }
}
