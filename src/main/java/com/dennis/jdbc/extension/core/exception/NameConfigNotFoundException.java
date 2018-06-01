package com.dennis.jdbc.extension.core.exception;

public class NameConfigNotFoundException extends RuntimeException {
    public NameConfigNotFoundException(String profile) {
        super(String.format("%s database configuration not found", profile));
    }
}
