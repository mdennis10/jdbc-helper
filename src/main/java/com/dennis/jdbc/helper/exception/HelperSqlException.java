package com.dennis.jdbc.helper.exception;

import java.sql.SQLException;

public class HelperSqlException extends RuntimeException {
    public HelperSqlException(SQLException throwable) {
        super(throwable);
    }
}
