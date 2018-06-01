package com.dennis.jdbc.extension.core;

import com.dennis.jdbc.extension.core.annotation.TypeData;
import com.dennis.jdbc.extension.core.exception.NameConfigNotFoundException;
import com.dennis.jdbc.extension.core.exception.NoColumnAnnotationException;
import com.google.common.base.Strings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    public void parseEntityNoColumnAnnotationTest() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.isClosed()).thenReturn(false);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        Executable executable = () -> helper.parseEntity(mockResultSet, Car.class);
        assertThrows(NoColumnAnnotationException.class, executable);
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

    @Test
    public void executeQueryNoSqlSupplied() {
        final DatabaseHelper helper = DatabaseHelper.getInstance();
        assertThrows(
            IllegalArgumentException.class,
            () -> helper.executeQuery(Book.class, "")
        );
    }

    @Test
    public void executeQueryNullSqlSupplied() {
        final DatabaseHelper helper = DatabaseHelper.getInstance();
        assertThrows(IllegalArgumentException.class, () -> helper.executeQuery(Book.class, null));
    }


    @Test
    public void executeQueryCollectionNoSqlSupplied() {
        final DatabaseHelper helper = DatabaseHelper.getInstance();
        assertThrows(IllegalArgumentException.class,() -> helper.executeQueryCollection(Book.class, ""));
    }

    @Test
    public void executeQueryCollectionNullSqlSupplied() {
        final DatabaseHelper helper = DatabaseHelper.getInstance();
        assertThrows(
            IllegalArgumentException.class,
            () -> helper.executeQueryCollection(Book.class, null)
        );
    }

    @Test
    public void executeQueryCollectionNullEntityTypeSupplied() {
        final DatabaseHelper helper = DatabaseHelper.getInstance();
        assertThrows(
            IllegalArgumentException.class,
            () -> helper.executeQueryCollection(null, "select * from Book")
        );
    }

    @Test
    public void executeQueryNullEntityTypeSupplied() {
        final DatabaseHelper helper = DatabaseHelper.getInstance();
        assertThrows(
            IllegalArgumentException.class,
            () -> helper.executeQuery(null, "select * from Book")
        );
    }

    @Test
    public void executeQueryCollectionInvalidNameConfigTest() {
        final DatabaseHelper helper = DatabaseHelper.getInstance("some-invalid-name");
        assertThrows(
            NameConfigNotFoundException.class,
            () -> helper.executeQueryCollection(Book.class, "select * from Book")
        );
    }
}
