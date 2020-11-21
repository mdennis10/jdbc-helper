package com.jdbc.helper;

import java.util.Map;

public interface ColumnMapper <T> {
    T map(Map<String, Object> resultSet);
}
