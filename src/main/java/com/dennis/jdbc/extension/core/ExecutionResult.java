package com.dennis.jdbc.extension.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class ExecutionResult {
    @Setter(AccessLevel.NONE)
    private Connection connection;

    @Setter(AccessLevel.NONE)
    private PreparedStatement preparedStatement;

    @Setter(AccessLevel.NONE)
    private ResultSet resultSet;
}
