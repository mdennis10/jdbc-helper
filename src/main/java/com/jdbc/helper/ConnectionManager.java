package com.jdbc.helper;

import javax.sql.DataSource;

public interface ConnectionManager {
    DataSource getDataSource(DbConfig config);
}
