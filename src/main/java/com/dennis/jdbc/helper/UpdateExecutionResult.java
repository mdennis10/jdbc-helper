package com.dennis.jdbc.helper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Data
public class UpdateExecutionResult extends ExecutionResult {
    @Setter(AccessLevel.NONE)
    private int rowsAffected;

    protected UpdateExecutionResult(
            Connection connection, PreparedStatement statement,
            ResultSet resultSet, int rowsAffected) {
        super(connection, statement, resultSet);
        this.rowsAffected = rowsAffected;
    }
}
