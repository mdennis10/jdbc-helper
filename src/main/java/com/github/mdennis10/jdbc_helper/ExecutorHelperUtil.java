package com.github.mdennis10.jdbc_helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ExecutorHelperUtil {
    protected static void resolveParameters(PreparedStatement preparedStatement, Object[] parameters) throws SQLException {
        for (int x = 0; x < parameters.length; x++) {
            preparedStatement.setObject(x + 1, parameters[x]);
        }
    }

    protected static Map<String, Object> parseRow(ResultSet resultSet) throws SQLException {
        Map<String, Object> columnMetaData = new HashMap<>();
        int column = resultSet.getMetaData().getColumnCount();
        for (int x = 1; x <= column;x++) {
            String columnName = resultSet.getMetaData().getColumnName(x);
            Object columnValue = resultSet.getObject(x);
            if(columnName != null) {
                columnMetaData.put(columnName.toUpperCase(), columnValue);
            }
        }
        return columnMetaData;
    }
}
