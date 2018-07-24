package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.exception.ConnectionException;
import com.dennis.jdbc.helper.util.ConnectionUtil;
import com.dennis.jdbc.helper.util.DbConfigurationUtil;
import com.google.common.base.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class DatabaseHelper_IntegrationTest {
    private static String author = "Mario Dennis";
    private final String nameConfig = "myprofile";
    private final long currentTime = Calendar.getInstance().getTime().getTime();

    @BeforeEach
    public void setup() throws SQLException {
        Connection defaultConnection = null;
        PreparedStatement preparedStatement = null;

        Connection myProfileConnection = null;
        PreparedStatement myProfileConnectionStatement = null;
        try {
            defaultConnection = DbConfigurationUtil.getTestConnection().get();
            preparedStatement = createBookDatabaseTable(defaultConnection);

            myProfileConnection = DbConfigurationUtil.getTestConnection(nameConfig).get();
            myProfileConnectionStatement = createBookDatabaseTable(myProfileConnection);
        } finally {
            ConnectionUtil.close(defaultConnection);
            ConnectionUtil.close(myProfileConnection);

            if (preparedStatement != null)
                preparedStatement.close();
            if(myProfileConnection != null)
                myProfileConnectionStatement.close();
        }
    }

    private PreparedStatement createBookDatabaseTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE Book( Author varchar(50), Created date )";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();

        String insertTableSQL = "INSERT INTO Book(Author, Created) VALUES(?,?)";
        preparedStatement = connection.prepareStatement(insertTableSQL);
        preparedStatement.setString(1, author);
        preparedStatement.setDate(2, new java.sql.Date(currentTime));
        preparedStatement.executeUpdate();
        return preparedStatement;
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

    @Test
    public void executeQueryDateTest() {
        Optional<Book> book = DatabaseHelper
                .getInstance()
                .executeQuery(Book.class,"SELECT * FROM Book WHERE Created = ?", new Date(currentTime));
        assertTrue(book.isPresent());
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        book = DatabaseHelper
                .getInstance()
                .executeQuery(Book.class, "SELECT * FROM Book WHERE Created > PARSEDATETIME(?, 'YYYY-MM-DD')", date);
        assertTrue(book.isPresent());

        book = DatabaseHelper
                .getInstance()
                .executeQuery(Book.class, "SELECT * FROM Book WHERE Created < PARSEDATETIME(?, 'YYYY-MM-DD')", date);
        assertFalse(book.isPresent());
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

    @AfterEach
    public void tearDown() throws SQLException {
        Connection profileConnection = null;
        PreparedStatement profilePreparedStatement = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            profileConnection = DbConfigurationUtil.getTestConnection(nameConfig).get();
            profilePreparedStatement = dropBookDatabaseTable(profileConnection);

            connection = DbConfigurationUtil.getTestConnection().get();
            preparedStatement = dropBookDatabaseTable(connection);
        } finally {
            ConnectionUtil.close(profileConnection);

            if (profilePreparedStatement != null)
                profilePreparedStatement.close();
        }
    }

    private PreparedStatement dropBookDatabaseTable(Connection connection) throws SQLException {
        String sql = "DROP TABLE Book";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
        return preparedStatement;
    }
}
