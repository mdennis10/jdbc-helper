package com.dennis.jdbc.extension.core;

import com.dennis.jdbc.extension.core.exception.ConnectionException;
import com.dennis.jdbc.extension.core.util.ConnectionUtil;
import com.dennis.jdbc.extension.core.util.DbConfigurationUtil;
import com.google.common.base.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static junit.framework.TestCase.*;

public class DatabaseHelper_IntegrationTest {
    private static String author = "Mario Dennis";
    private final String nameConfig = "myprofile";

    @Before
    public void setup() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            String sql = "CREATE TABLE Book( Author varchar(50))";
            Optional<Connection> factory = DbConfigurationUtil.getTestConnection();
            if (!factory.isPresent())
                return;
            else
                connection = factory.get();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

            String insertTableSQL = "INSERT INTO Book(Author) VALUES(?)";
            preparedStatement = connection.prepareStatement(insertTableSQL);
            preparedStatement.setString(1, author);
            preparedStatement.executeUpdate();
        } finally {
            if (!ConnectionUtil.isClosed(connection))
                connection.close();

            if (preparedStatement != null)
                preparedStatement.close();
        }
    }


    @Test
    public void executeQueryUsingConfigFileTest() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        Optional<Book> book = helper.executeQuery(Book.class, "SELECT * FROM Book");
        assertNotNull(book);
        assertTrue(book.isPresent());
        assertEquals(author, book.get().getAuthor());
    }

    @Test
    public void executeQueryUsingNameConfigTest() {
        DatabaseHelper helper = DatabaseHelper.getInstance(nameConfig);
        Optional<Book> book = helper.executeQuery(Book.class, "SELECT * FROM Book");
        assertNotNull(book);
        assertTrue(book.isPresent());
        assertEquals(author, book.get().getAuthor());
    }


    @Test
    public void executeQueryCollectionUsingConfigFileTest() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        List<Book> book = helper.executeQueryCollection(Book.class, "SELECT * FROM Book");
        assertNotNull(book);
        assertFalse(book.isEmpty());
        assertEquals(author, book.get(0).getAuthor());
    }

    @Test
    public void executeQueryCollectionUsingNameConfigTest() {
        DatabaseHelper helper = DatabaseHelper.getInstance(nameConfig);
        List<Book> book = helper.executeQueryCollection(Book.class, "SELECT * FROM Book");
        assertNotNull(book);
        assertFalse(book.isEmpty());
        assertEquals(author, book.get(0).getAuthor());
    }

    @Test
    public void executeUpdateQueryUsingConfigFileTest() throws SQLException {
        DatabaseHelper helper = DatabaseHelper.getInstance();

        String author = "some-random-author-name";
        int rowsAffected = helper.executeUpdateQuery("INSERT INTO Book(Author) VALUES(?)", author);
        assertTrue(rowsAffected > 0);
        assertTrue(isBookByAuthorExist(author));
    }

    private boolean isBookByAuthorExist(String author) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sql = "SELECT Author FROM Book WHERE Author=?";
            Optional<Connection> factory = DbConfigurationUtil.getTestConnection();
            if (!factory.isPresent())
                throw new ConnectionException();
            else
                connection = factory.get();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, author);
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } finally {
            ConnectionUtil.close(connection);
            ConnectionUtil.close(resultSet);
        }
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            String sql = "DROP TABLE Book";
            Optional<Connection> factory = DbConfigurationUtil.getTestConnection();
            if (!factory.isPresent())
                return;
            else
                connection = factory.get();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } finally {
            if (ConnectionUtil.isClosed(connection))
                connection.close();

            if (preparedStatement != null)
                preparedStatement.close();
        }
    }
}
