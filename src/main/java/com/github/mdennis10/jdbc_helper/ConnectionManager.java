package com.github.mdennis10.jdbc_helper;

import javax.sql.DataSource;
import java.io.Closeable;

public interface ConnectionManager extends Closeable {
    DataSource getDataSource(DbConfig config);
}
