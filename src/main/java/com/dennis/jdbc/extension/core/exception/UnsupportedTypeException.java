package com.dennis.jdbc.extension.core.exception;

public class UnsupportedTypeException extends RuntimeException {
    public UnsupportedTypeException(Class<?> clazz) {
        super(String.format("%s is not a supported type remove @Column annotation", clazz.getName()));
    }
}
