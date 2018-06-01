package com.dennis.jdbc.extension.core.exception;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Ignore
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
