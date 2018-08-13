package com.dennis.jdbc.helper.exception;

import java.sql.SQLException;

public class HelperSQLException extends RuntimeException {
    public HelperSQLException(SQLException throwable) {
        super(throwable);
    }
}
