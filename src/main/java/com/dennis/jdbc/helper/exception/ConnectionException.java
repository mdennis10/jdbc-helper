package com.dennis.jdbc.helper.exception;

public class ConnectionException extends RuntimeException {
    public ConnectionException() {
        super("Unable to establish connection");
    }

    public ConnectionException(String msg) {
        super(msg);
    }
}
