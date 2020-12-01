package com.github.mdennis10.jdbc_helper;

import java.util.Map;

public interface ColumnMapper <T> {
    T map(Map<String, Object> resultSet);
}
