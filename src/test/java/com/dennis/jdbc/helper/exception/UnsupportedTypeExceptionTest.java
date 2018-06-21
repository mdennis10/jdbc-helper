package com.dennis.jdbc.helper.exception;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnsupportedTypeExceptionTest {
    @Test
    public void unsupportedTypeExceptionTest() {
        try {
            throw new UnsupportedTypeException(boolean.class);
        } catch (UnsupportedTypeException e) {
            assertEquals("boolean is not a supported type remove @Column annotation", e.getMessage());
        }
    }

}
