package com.dennis.jdbc.extension.core;

import com.dennis.jdbc.extension.core.annotation.TypeData;
import com.dennis.jdbc.extension.core.exception.NameConfigNotFoundException;
import com.dennis.jdbc.extension.core.exception.NoColumnAnnotationException;
import com.google.common.base.Strings;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DatabaseHelperTest {

    @Test
    public void closeTest() throws SQLException {
        ExecutionResult mockExecutionResult = mock(ExecutionResult.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockExecutionResult.getConnection()).thenReturn(mockConnection);
        when(mockExecutionResult.getResultSet()).thenReturn(mockResultSet);
        when(mockExecutionResult.getPreparedStatement()).thenReturn(mockPreparedStatement);
        when(mockConnection.isClosed()).thenReturn(false);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.close(mockExecutionResult);
        verify(mockConnection, times(1)).isClosed();// ensure the connection status is checked before closed
        verify(mockConnection, times(1)).close();
        verify(mockPreparedStatement, times(1)).close();
        verify(mockResultSet, times(1)).close();
    }


    @Test
    public void parseEntityTest() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.isClosed()).thenReturn(false);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Author")).thenReturn("Mario Dennis");

        DatabaseHelper helper = DatabaseHelper.getInstance();
        List<Book> books = helper.parseEntity(mockResultSet, Book.class);
        assertNotNull(books);
        assertFalse(books.isEmpty());

        Book book = books.get(0);
        assertNotNull(book);
        assertFalse(Strings.isNullOrEmpty(book.getAuthor()));
        assertEquals("Mario Dennis", book.getAuthor());
    }

    @Test(expected = NoColumnAnnotationException.class)
    public void parseEntityNoColumnAnnotationTest() throws SQLException {
        final ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.isClosed()).thenReturn(false);

        final DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.parseEntity(mockResultSet, Car.class);
    }

    @Test
    public void setFieldTest() throws SQLException {
        Book book = new Book();

        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getString("Author")).thenReturn("John");

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, book, new TypeData("Author", String.class, "author"));

        assertFalse(Strings.isNullOrEmpty(book.getAuthor()));
        assertEquals("John", book.getAuthor());
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeQueryNoSqlSupplied() {
        final DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.executeQuery(Book.class, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeQueryNullSqlSupplied() {
        final DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.executeQuery(Book.class, null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void executeQueryCollectionNoSqlSupplied() {
        final DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.executeQueryCollection(Book.class, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeQueryCollectionNullSqlSupplied() {
        final DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.executeQueryCollection(Book.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeQueryCollectionNullEntityTypeSupplied() {
        final DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.executeQueryCollection(null, "select * from Book");
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeQueryNullEntityTypeSupplied() {
        final DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.executeQuery(null, "select * from Book");
    }

    @Test(expected = NameConfigNotFoundException.class)
    public void executeQueryCollectionInvalidNameConfigTest() {
        final DatabaseHelper helper = DatabaseHelper.getInstance("some-invalid-name");
        helper.executeQueryCollection(Book.class, "select * from Book");
    }
}
