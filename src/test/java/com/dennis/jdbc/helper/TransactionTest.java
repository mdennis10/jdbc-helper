package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.exception.ConnectionException;
import com.dennis.jdbc.helper.util.ConnectionUtil;
import com.dennis.jdbc.helper.util.DbConfigurationUtil;
import com.google.common.base.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {
    @BeforeEach
    public void setup() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            String sql = "CREATE TABLE Book( Author varchar(50), Created date )";
            Optional<Connection> factory = DbConfigurationUtil.getTestConnection();
            if (!factory.isPresent())
                return;
            else
                connection = factory.get();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } finally {
            if (!ConnectionUtil.isClosed(connection))
                connection.close();

            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @Test
    public void commitTest() throws SQLException{
        Transaction transaction = Transaction.get();
        try {
            StringBuilder bookInsertSql = new StringBuilder();
            bookInsertSql.append("INSERT INTO Book(Author, Created) ");
            bookInsertSql.append("VALUES(?, ?)");
            Date created = new Date(new java.util.Date().getTime());

            int rowAffected1 = transaction.executeUpdateQuery(bookInsertSql.toString(), "Tom", created);
            StringBuilder bookInsertSql2 = new StringBuilder();
            bookInsertSql2.append("INSERT INTO Book(Author, Created) ");
            bookInsertSql2.append("VALUES(?,?)");
            int rowsAffected2 = transaction.executeUpdateQuery(bookInsertSql2.toString(), "Jerry", created);
            int expectedRowsAffected = 1;

            // 1 row should be affected
            assertEquals(expectedRowsAffected, rowAffected1);
            assertEquals(expectedRowsAffected, rowsAffected2);

            // should be false since transaction is not committed
            assertFalse(getBookByAuthor("Tom").isPresent());
            assertFalse(getBookByAuthor("Jerry").isPresent());

            transaction.commit();

            assertTrue(getBookByAuthor("Tom").isPresent());
            assertTrue(getBookByAuthor("Jerry").isPresent());
        } finally {
            transaction.close();
        }

    }

    public Optional<Book> getBookByAuthor(String author) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            String sql = "Select * from Book where Author = ?";
            Optional<Connection> factory = DbConfigurationUtil.getTestConnection();
            if (!factory.isPresent())
                throw new ConnectionException();
            else
                connection = factory.get();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, author);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Book book = new Book();
                book.setAuthor(resultSet.getString("Author"));
                return Optional.of(book);
            }
            return Optional.absent();
        } finally {
            if (!ConnectionUtil.isClosed(connection))
                connection.close();

            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @AfterEach
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
