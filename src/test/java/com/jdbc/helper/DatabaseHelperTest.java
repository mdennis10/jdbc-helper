package com.jdbc.helper;

import com.jdbc.helper.model.Person;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
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

public class DatabaseHelperTest {
    private static final DbConfig config = new DbConfig(
            "sa",
            "pass@4d1",
            "jdbc:h2:file:~/helper_test",
            "org.h2.Driver"
    );

    @BeforeAll public static void setup() throws SQLException, ClassNotFoundException {
        SqlUtil.executeUpdate(config, "CREATE TABLE Person(name varchar(50))");
    }

    @AfterAll public static void tearDown() throws SQLException, ClassNotFoundException, IOException {
        SqlUtil.executeUpdate(config, "DROP TABLE Person");
        new DatabaseHelper(config).close();
    }

    @Test void executeUpdate() throws SQLException, ClassNotFoundException {
        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(config);
            int result = databaseHelper.executeUpdate("INSERT INTO Person(name) VALUES(?)", new Object[]{"JaneDoe"});
            assertEquals(1, result);
        } finally {
            SqlUtil.executeUpdate(config, "DELETE FROM Person");
        }
    }

    @Test void executeUpdate_invalidParameters() throws SQLException, ClassNotFoundException {
        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(config);
            int result = databaseHelper.executeUpdate("CREATE TABLE Items(name varchar(50))", null);
            assertEquals(0, result);

            // assert null sql parameter
            String expected = "Null or empty sql argument supplied";
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> databaseHelper.executeUpdate(null, null));
            assertEquals(expected, exception.getMessage());

            // assert empty sql parameter
            exception = assertThrows(IllegalArgumentException.class,() -> databaseHelper.executeUpdate("", null));
            assertEquals(expected, exception.getMessage());
        } finally {
            SqlUtil.executeUpdate(config, "Drop Table Items");
        }
    }


    @Test void executeUpdate_resourceCleanUp() throws SQLException {
        String sql = "CREATE TABLE User(name varchar(50))";
        ConnectionManager mockConnectionManager = mock(ConnectionManager.class);
        DataSource mockDataSource = mock(DataSource.class);
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPrepStmt = mock(PreparedStatement.class);

        when(mockConnectionManager.getDataSource(config)).thenReturn(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(sql)).thenReturn(mockPrepStmt);
        when(mockPrepStmt.executeUpdate()).thenReturn(0);

        DatabaseHelper databaseHelper = new DatabaseHelper(config);
        databaseHelper.setConnectionManager(mockConnectionManager);
        databaseHelper.executeUpdate(sql, new Object[]{});
        verify(mockConnection, atLeastOnce()).close();
        verify(mockPrepStmt, atLeastOnce()).close();
    }


    @Test public void executeUpdate_noSqlSupplied() {
        DatabaseHelper databaseHelper = new DatabaseHelper(config);
        String expected = "Null or empty sql argument supplied";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> databaseHelper.executeUpdate(null, new Object[]{}));
        assertEquals(expected, exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> databaseHelper.executeUpdate("", new Object[]{}));
        assertEquals(expected, exception.getMessage());
    }

    @Test void query_overloadedWithMapperArgument() throws SQLException, ClassNotFoundException {
        assert SqlUtil.executeUpdate(config, "INSERT INTO Person(NAME) VALUES('JOHN')") > 0;
        DatabaseHelper databaseHelper = new DatabaseHelper(config);
        String sql = "SELECT NAME FROM Person WHERE NAME=?";
        ColumnMapper<String> mapper = x -> String.valueOf(x.get("NAME"));

        Optional<String> result = databaseHelper.query(sql, new Object[]{"JOHN"}, mapper);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals("JOHN", result.get());

        result = databaseHelper.query(sql, new Object[]{"SomeValueThatDoesNotExist"}, mapper);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test void query_overloadedWithMapperArgument_resourceCleanUp() throws SQLException {
        String sql = "SELECT * FROM PERSON WHERE NAME=?";
        ConnectionManager mockConnectionManager = mock(ConnectionManager.class);
        DataSource mockDataSource = mock(DataSource.class);
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPrepStmt = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);


        when(mockConnectionManager.getDataSource(config)).thenReturn(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(sql)).thenReturn(mockPrepStmt);
        when(mockPrepStmt.executeQuery()).thenReturn(mockResultSet);

        DatabaseHelper databaseHelper = new DatabaseHelper(config);
        databaseHelper.setConnectionManager(mockConnectionManager);
        databaseHelper.query(sql, new Object[]{"JOHN"}, rs -> rs.get("name"));
        verify(mockConnection, atLeastOnce()).close();
        verify(mockPrepStmt, atLeastOnce()).close();
        verify(mockResultSet, atLeastOnce()).close();
    }


    @Test void query_overloadedWithMapperArgument_invalidArguments() {
        String sql = "SELECT * FROM Person WHERE name=?";
        ColumnMapper<String> mapper = x -> String.valueOf(x.get("NAME"));

        DatabaseHelper databaseHelper = new DatabaseHelper(config);
        NullPointerException nullPointerException = assertThrows(
            NullPointerException.class,
            () -> databaseHelper.query(sql, null, mapper)
        );
        assertEquals("Null SQL parameter arguments supplied", nullPointerException.getMessage());

        // assert null sql parameter
        String expected = "Null or empty sql argument supplied";
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> databaseHelper.query(null, new Object[]{"JOHN"}, mapper)
        );
        assertEquals(expected, exception.getMessage());

        // assert empty sql parameter
        exception = assertThrows(
            IllegalArgumentException.class,
            () -> databaseHelper.query("", new Object[]{"JOHN"}, mapper)
        );
        assertEquals(expected, exception.getMessage());

        // assert null mapper
        nullPointerException = assertThrows(
                NullPointerException.class,
                () -> databaseHelper.query(sql, new Object[]{"JOHN"}, null)
        );
        assertEquals("Null mapper supplied", nullPointerException.getMessage());
    }

    @Test void queryForList_overloadedWithMapperArgument() throws SQLException, ClassNotFoundException {
        try {
            assert SqlUtil.executeUpdate(config, "INSERT INTO Person(name) VALUES('JOHN')") > 0;
            assert SqlUtil.executeUpdate(config, "INSERT INTO Person(name) VALUES('JANE')") > 0;

            DatabaseHelper databaseHelper = new DatabaseHelper(config);
            ColumnMapper<String> mapper = x -> String.valueOf(x.get("NAME"));
            List<String> result = databaseHelper.queryForList(
                "SELECT * FROM Person",
                    new Object[]{},
                    mapper
            );
            assertNotNull(result);
            assertFalse(result.isEmpty());

            // assert no item was found
            result = databaseHelper.queryForList(
            "SELECT * FROM Person WHERE NAME =?",
                new Object[]{"ValueThatDoesNotExist"},
                mapper
            );
            assertNotNull(result);
            assertTrue(result.isEmpty());
        } finally {
            SqlUtil.executeUpdate(config, "DELETE FROM Person");
        }
    }

    @Test void queryForList_overloadedWithMapperArgument_invalidParameters() {
        String sql = "SELECT * FROM Person";
        ColumnMapper<String> mapper = x -> String.valueOf(x.get("NAME"));

        DatabaseHelper databaseHelper = new DatabaseHelper(config);
        NullPointerException nullPointerException = assertThrows(
                NullPointerException.class,
                () -> databaseHelper.queryForList(sql, null, mapper)
        );
        assertEquals("Null SQL parameter arguments supplied", nullPointerException.getMessage());

        // assert null sql parameter
        String expected = "Null or empty sql argument supplied";
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> databaseHelper.queryForList(null, new Object[]{"JOHN"}, mapper)
        );
        assertEquals(expected, exception.getMessage());

        // assert empty sql parameter
        exception = assertThrows(
                IllegalArgumentException.class,
                () -> databaseHelper.queryForList("", new Object[]{"JOHN"}, mapper)
        );
        assertEquals(expected, exception.getMessage());

        // assert null mapper
        nullPointerException = assertThrows(
                NullPointerException.class,
                () -> databaseHelper.queryForList(sql, new Object[]{"JOHN"}, null)
        );
        assertEquals("Null mapper supplied", nullPointerException.getMessage());
    }

    @Test void queryForList_overloadedWithMapperArgument_resourceCleanUp() throws SQLException {
        String sql = "SELECT * FROM PERSON";
        ConnectionManager mockConnectionManager = mock(ConnectionManager.class);
        DataSource mockDataSource = mock(DataSource.class);
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPrepStmt = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnectionManager.getDataSource(config)).thenReturn(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(sql)).thenReturn(mockPrepStmt);
        when(mockPrepStmt.executeQuery()).thenReturn(mockResultSet);

        DatabaseHelper databaseHelper = new DatabaseHelper(config);
        databaseHelper.setConnectionManager(mockConnectionManager);
        databaseHelper.queryForList(sql, new Object[]{}, x -> String.valueOf(x.get("NAME")));
        verify(mockConnection, atLeastOnce()).close();
        verify(mockPrepStmt, atLeastOnce()).close();
        verify(mockResultSet, atLeastOnce()).close();
    }

    @Test void query() throws SQLException, ClassNotFoundException {
        try {
            assert SqlUtil.executeUpdate(config, "INSERT INTO Person(NAME) VALUES('JOHN')") > 0;
            DatabaseHelper databaseHelper = new DatabaseHelper(config);
            String sql = "SELECT NAME FROM Person WHERE NAME=?";

            Optional<Person> result = databaseHelper.query(Person.class, sql, new Object[]{"JOHN"});
            assertNotNull(result);
            assertTrue(result.isPresent());
            assertEquals("JOHN", result.get().getName());

            result = databaseHelper.query(Person.class, sql, new Object[]{"SomeValueThatDoesNotExist"});
            assertNotNull(result);
            assertFalse(result.isPresent());
        } finally {
            SqlUtil.executeUpdate(config, "DELETE FROM Person");
        }
    }

    @Test void query_resourceCleanUp() throws SQLException {
        String sql = "SELECT * FROM PERSON WHERE NAME=?";
        ConnectionManager mockConnectionManager = mock(ConnectionManager.class);
        DataSource mockDataSource = mock(DataSource.class);
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPrepStmt = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);


        when(mockConnectionManager.getDataSource(config)).thenReturn(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(sql)).thenReturn(mockPrepStmt);
        when(mockPrepStmt.executeQuery()).thenReturn(mockResultSet);

        DatabaseHelper databaseHelper = new DatabaseHelper(config);
        databaseHelper.setConnectionManager(mockConnectionManager);
        databaseHelper.query(Person.class, sql, new Object[]{"JOHN"});
        verify(mockConnection, atLeastOnce()).close();
        verify(mockPrepStmt, atLeastOnce()).close();
        verify(mockResultSet, atLeastOnce()).close();
    }


    @Test void query_invalidArguments() {
        String sql = "SELECT * FROM Person WHERE name=?";

        DatabaseHelper databaseHelper = new DatabaseHelper(config);
        NullPointerException nullPointerException = assertThrows(
                NullPointerException.class,
                () -> databaseHelper.query(Person.class, sql, null)
        );
        assertEquals("Null SQL parameter arguments supplied", nullPointerException.getMessage());

        //assert null clazz parameter
        nullPointerException = assertThrows(
                NullPointerException.class,
                () -> databaseHelper.query(null, sql, new Object[]{"JOHN"})
        );
        assertEquals("Null clazz argument supplied", nullPointerException.getMessage());

        // assert null sql parameter
        String expected = "Null or empty sql argument supplied";
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> databaseHelper.query(Person.class, null, new Object[]{"JOHN"})
        );
        assertEquals(expected, exception.getMessage());

        // assert empty sql parameter
        exception = assertThrows(
                IllegalArgumentException.class,
                () -> databaseHelper.query(Person.class, "", new Object[]{"JOHN"})
        );
        assertEquals(expected, exception.getMessage());
    }

    @Test void queryForList() throws SQLException, ClassNotFoundException {
        try {
            assert SqlUtil.executeUpdate(config, "INSERT INTO Person(name) VALUES('JOHN')") > 0;
            assert SqlUtil.executeUpdate(config, "INSERT INTO Person(name) VALUES('JANE')") > 0;

            DatabaseHelper databaseHelper = new DatabaseHelper(config);
            List<Person> result = databaseHelper.queryForList(
                    Person.class,
                    "SELECT * FROM Person",
                    new Object[]{}
            );
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("JOHN", result.get(0).getName());
            assertEquals("JANE", result.get(1).getName());

            // assert no item was found
            result = databaseHelper.queryForList(
                    Person.class,
                    "SELECT * FROM Person WHERE NAME =?",
                    new Object[]{"ValueThatDoesNotExist"}
            );
            assertNotNull(result);
            assertTrue(result.isEmpty());
        } finally {
            SqlUtil.executeUpdate(config, "DELETE FROM Person");
        }
    }

    @Test void queryForList_invalidParameters() {
        String sql = "SELECT * FROM Person";

        DatabaseHelper databaseHelper = new DatabaseHelper(config);
        NullPointerException nullPointerException = assertThrows(
                NullPointerException.class,
                () -> databaseHelper.queryForList(Person.class, sql, null)
        );
        assertEquals("Null SQL parameter arguments supplied", nullPointerException.getMessage());

        nullPointerException = assertThrows(
                NullPointerException.class,
                () -> databaseHelper.queryForList(null, sql, new Object[]{})
        );
        assertEquals("Null clazz argument supplied", nullPointerException.getMessage());

        // assert null sql parameter
        String expected = "Null or empty sql argument supplied";
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> databaseHelper.queryForList(Person.class, null, new Object[]{"JOHN"})
        );
        assertEquals(expected, exception.getMessage());

        // assert empty sql parameter
        exception = assertThrows(
                IllegalArgumentException.class,
                () -> databaseHelper.queryForList(Person.class, "", new Object[]{"JOHN"})
        );
        assertEquals(expected, exception.getMessage());
    }

    @Test void queryForList_resourceCleanUp() throws SQLException {
        String sql = "SELECT * FROM PERSON";
        ConnectionManager mockConnectionManager = mock(ConnectionManager.class);
        DataSource mockDataSource = mock(DataSource.class);
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPrepStmt = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnectionManager.getDataSource(config)).thenReturn(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(sql)).thenReturn(mockPrepStmt);
        when(mockPrepStmt.executeQuery()).thenReturn(mockResultSet);

        DatabaseHelper databaseHelper = new DatabaseHelper(config);
        databaseHelper.setConnectionManager(mockConnectionManager);
        databaseHelper.queryForList(Person.class, sql, new Object[]{});
        verify(mockConnection, atLeastOnce()).close();
        verify(mockPrepStmt, atLeastOnce()).close();
        verify(mockResultSet, atLeastOnce()).close();
    }

    @Test void executeBatchUpdate() throws SQLException, ClassNotFoundException {
        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(config);
            List<Object[]> parameters = new ArrayList<>();
            parameters.add(new Object [] {"Jane Doe"});
            parameters.add(new Object [] {"John Doe"});
            parameters.add(new Object [] {"Jack Doe"});
            int[] result = databaseHelper.executeBatchUpdate(
                "INSERT INTO Person(name) VALUES(?)",
                    parameters
            );
            assertEquals(3, result.length);
            for(int x : result) {
                assertTrue(x > 0);
            }
        } finally {
            SqlUtil.executeUpdate(config, "DELETE FROM Person");
        }
    }

    @Test void executeBatchUpdate_invalidParameters() throws SQLException, ClassNotFoundException {
        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(config);
            int[] result = databaseHelper.executeBatchUpdate(
                "CREATE TABLE Items(name varchar(50))", new ArrayList<>()
            );
            assertEquals(1, result.length);

            // assert null sql parameter
            String expected = "Null or empty sql argument supplied";
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> databaseHelper.executeBatchUpdate(null, null)
            );
            assertEquals(expected, exception.getMessage());

            // assert empty sql parameter
            exception = assertThrows(
                IllegalArgumentException.class,
                () -> databaseHelper.executeBatchUpdate("", null)
            );
            assertEquals(expected, exception.getMessage());
        } finally {
            SqlUtil.executeUpdate(config, "Drop Table Items");
        }
    }


    @Test void executeBatchUpdate_resourceCleanUp() throws SQLException {
        String sql = "CREATE TABLE User(name varchar(50))";
        ConnectionManager mockConnectionManager = mock(ConnectionManager.class);
        DataSource mockDataSource = mock(DataSource.class);
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPrepStmt = mock(PreparedStatement.class);

        when(mockConnectionManager.getDataSource(config)).thenReturn(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(sql)).thenReturn(mockPrepStmt);
        when(mockPrepStmt.executeUpdate()).thenReturn(0);

        DatabaseHelper databaseHelper = new DatabaseHelper(config);
        databaseHelper.setConnectionManager(mockConnectionManager);
        databaseHelper.executeBatchUpdate(sql, new ArrayList<>());
        verify(mockConnection, atLeastOnce()).close();
        verify(mockPrepStmt, atLeastOnce()).close();
    }


    @Test public void executeBatchUpdate_noSqlSupplied() {
        DatabaseHelper databaseHelper = new DatabaseHelper(config);
        String expected = "Null or empty sql argument supplied";
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> databaseHelper.executeBatchUpdate(null, new ArrayList<>())
        );
        assertEquals(expected, exception.getMessage());

        exception = assertThrows(
            IllegalArgumentException.class,
            () -> databaseHelper.executeBatchUpdate("", new ArrayList<>())
        );
        assertEquals(expected, exception.getMessage());
    }
}
