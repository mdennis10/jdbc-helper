package com.dennis.jdbc.helper.util;


import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ConnectionUtilTest {
    @Test
    public void isConnectionClosedTest() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.isClosed()).thenReturn(true);
        assertTrue(ConnectionUtil.isClosed(connection));
    }

    @Test
    public void isResultSetCloseTest() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.isClosed()).thenReturn(true);
        assertTrue(ConnectionUtil.isClosed(mockResultSet));
    }

    @Test
    public void closeConnectionTest() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.isClosed()).thenReturn(false);
        ConnectionUtil.close(connection);
        verify(connection).close();
    }

    @Test
    public void closeResultSetTest() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        ConnectionUtil.close(resultSet);
        verify(resultSet).close();
    }
}
