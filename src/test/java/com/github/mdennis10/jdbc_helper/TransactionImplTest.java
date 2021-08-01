package com.github.mdennis10.jdbc_helper;

import com.github.mdennis10.jdbc_helper.model.Person;
import com.google.common.base.Strings;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionImplTest {
    private static final DbConfig config = new DbConfig(
            "sa",
            "pass@4d1",
            "jdbc:h2:file:~/helper_test",
            "org.h2.Driver"
    );

    @BeforeAll
    public static void setup() throws SQLException, ClassNotFoundException {
        SqlUtil.executeUpdate(config, "CREATE TABLE Person(name varchar(50))");
    }

    @AfterAll
    public static void tearDown() throws SQLException, ClassNotFoundException, IOException {
        SqlUtil.executeUpdate(config, "DROP TABLE Person");
        DatabaseHelper.close();
    }

    @Test void begin_ConnectionAutoCommitDisabled() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        UpdateExecutor mockUpdateExecutor = mock(UpdateExecutor.class);
        QueryExecutor mockQueryExecutor = mock(QueryExecutor.class);
        Transaction transaction = new TransactionImpl(
                mockConnection, mockUpdateExecutor, mockQueryExecutor
        );
        verify(mockConnection, times(1)).setAutoCommit(false);
    }

    @Test void rollback_connectionRollbackMethodInvoked() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        UpdateExecutor mockUpdateExecutor = mock(UpdateExecutor.class);
        QueryExecutor mockQueryExecutor = mock(QueryExecutor.class);
        Transaction transaction = new TransactionImpl(
            mockConnection, mockUpdateExecutor, mockQueryExecutor
        );
        transaction.rollback();
        verify(mockConnection, times(1)).rollback();
    }

    @Test void commit_connectionCommitMethodInvoked() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        UpdateExecutor mockUpdateExecutor = mock(UpdateExecutor.class);
        QueryExecutor mockQueryExecutor = mock(QueryExecutor.class);
        Transaction transaction = new TransactionImpl(
                mockConnection, mockUpdateExecutor, mockQueryExecutor
        );
        transaction.commit();
        verify(mockConnection, times(1)).commit();
        verify(mockConnection, times(1)).close();
    }

    @Test void executeUpdate() throws SQLException, ClassNotFoundException {
        try {
            Connection connection = SqlUtil.getConnection(config);
            Transaction transaction = new TransactionImpl(
                connection, new UpdateExecutor(), new QueryExecutor()
            );
            assertEquals(1, transaction.executeUpdate("INSERT INTO Person(name) VALUES(?)", new Object[]{"John"}));
            assertEquals(1, transaction.executeUpdate("INSERT INTO Person(name) VALUES(?)", new Object[]{"Jane"}));
            assertEquals(2, transaction.executeUpdate("UPDATE Person SET name=?", new Object[]{"Some Random name"}));
            transaction.commit();
        } finally {
            SqlUtil.executeUpdate(config, "DELETE FROM Person");
        }
    }

    @Test void executeUpdate_changesAreNotSavedWithoutCallingCommit() throws SQLException, ClassNotFoundException {
        try (Connection connection = SqlUtil.getConnection(config)){
            Transaction transaction = new TransactionImpl(
                connection, new UpdateExecutor(), new QueryExecutor()
            );
            transaction.executeUpdate("INSERT INTO Person(name) VALUES(?)", new Object[]{"John"});
            transaction.executeUpdate("INSERT INTO Person(name) VALUES(?)", new Object[]{"Jane"});
            //query to ensure data was not saved for uncommitted transaction
            try (Connection mConnection = SqlUtil.getConnection(config);
                 PreparedStatement preparedStatement = mConnection.prepareStatement("SELECT * FROM Person")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Person> persons = new ArrayList<>();
                while(resultSet.next()) {
                    Person person = new Person();
                    person.setName(resultSet.getString("name"));
                    persons.add(person);
                }
                assertTrue(persons.isEmpty());
            }
        } finally {
            SqlUtil.executeUpdate(config, "DELETE FROM Person");
        }
    }

    @Test void executeQuery () throws SQLException, ClassNotFoundException {
        try {
            assert SqlUtil.executeUpdate(config, "INSERT INTO Person(name) VALUES('JOHN')") > 0;
            Connection connection = SqlUtil.getConnection(config);
            Transaction transaction = new TransactionImpl(
                connection, new UpdateExecutor(), new QueryExecutor()
            );
            Optional<Person> result = transaction.query(Person.class, "SELECT * FROM Person WHERE name=?", new Object[]{"JOHN"});
            transaction.commit();
            assertNotNull(result);
            assertTrue(result.isPresent());
            assertFalse(Strings.isNullOrEmpty(result.get().getName()));
        } finally {
            SqlUtil.executeUpdate(config, "DELETE FROM Person");
        }
    }

}
