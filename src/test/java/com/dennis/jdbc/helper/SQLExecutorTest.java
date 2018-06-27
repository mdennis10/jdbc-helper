package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.util.ConnectionUtil;
import com.dennis.jdbc.helper.util.DbConfigurationUtil;
import com.google.common.base.Optional;
import org.h2.expression.Parameter;
import org.h2.jdbc.JdbcParameterMetaData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class SQLExecutorTest {
    private final String author = "Mario Dennis";

    @BeforeEach
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
    public void parseIntParameterTest() throws SQLException {
        Optional<Connection> factory = DbConfigurationUtil.getTestConnection();
        Connection connection = factory.get();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM Book where author =?");
            int param = Integer.MAX_VALUE;
            SQLExecutor.parseParameters(statement, param);
            ParameterMetaData metaData = statement.getParameterMetaData();

            Field field = JdbcParameterMetaData.class.getDeclaredField("parameters");
            field.setAccessible(true);
            ArrayList<Parameter> params = (ArrayList) field.get(metaData);
            int value = params.get(0).getParamValue().getInt();
            assertEquals(param, value);
        } catch (SQLException e) {
           if(connection != null && !connection.isClosed()) {
               connection.close();
           } if(statement != null) {
               statement.close();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseDoubleParameterTest() throws SQLException {
        Optional<Connection> factory = DbConfigurationUtil.getTestConnection();
        Connection connection = factory.get();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM Book where author =?");
            double param = Double.MAX_VALUE;
            SQLExecutor.parseParameters(statement, param);
            ParameterMetaData metaData = statement.getParameterMetaData();

            Field field = JdbcParameterMetaData.class.getDeclaredField("parameters");
            field.setAccessible(true);
            ArrayList<Parameter> params = (ArrayList) field.get(metaData);
            double value = params.get(0).getParamValue().getDouble();
            assertEquals(param, value);
        } catch (SQLException e) {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            } if(statement != null) {
                statement.close();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseStringParameterTest() throws SQLException {
        Optional<Connection> factory = DbConfigurationUtil.getTestConnection();
        Connection connection = factory.get();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM Book where author =?");
            String param = author;
            SQLExecutor.parseParameters(statement, param);
            ParameterMetaData metaData = statement.getParameterMetaData();

            Field field = JdbcParameterMetaData.class.getDeclaredField("parameters");
            field.setAccessible(true);
            ArrayList<Parameter> params = (ArrayList) field.get(metaData);
            String value = params.get(0).getParamValue().getString();
            assertEquals(param, value);
        } catch (SQLException e) {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            } if(statement != null) {
                statement.close();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseFloatParameterTest() throws SQLException {
        Optional<Connection> factory = DbConfigurationUtil.getTestConnection();
        Connection connection = factory.get();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM Book where author =?");
            Float param = Float.MAX_VALUE;
            SQLExecutor.parseParameters(statement, param);
            ParameterMetaData metaData = statement.getParameterMetaData();

            Field field = JdbcParameterMetaData.class.getDeclaredField("parameters");
            field.setAccessible(true);
            ArrayList<Parameter> params = (ArrayList) field.get(metaData);
            Float value = params.get(0).getParamValue().getFloat();
            assertEquals(param, value);
        } catch (SQLException e) {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            } if(statement != null) {
                statement.close();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseBooleanParameterTest() throws SQLException {
        Optional<Connection> factory = DbConfigurationUtil.getTestConnection();
        Connection connection = factory.get();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM Book where author =?");
            Boolean param = Boolean.TRUE;
            SQLExecutor.parseParameters(statement, param);
            ParameterMetaData metaData = statement.getParameterMetaData();

            Field field = JdbcParameterMetaData.class.getDeclaredField("parameters");
            field.setAccessible(true);
            ArrayList<Parameter> params = (ArrayList) field.get(metaData);
            Boolean value = params.get(0).getParamValue().getBoolean();
            assertEquals(param, value);
        } catch (SQLException e) {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            } if(statement != null) {
                statement.close();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void parseLongParameterTest() throws SQLException {
        Optional<Connection> factory = DbConfigurationUtil.getTestConnection();
        Connection connection = factory.get();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM Book where author =?");
            Long param = Long.MAX_VALUE;
            SQLExecutor.parseParameters(statement, param);
            ParameterMetaData metaData = statement.getParameterMetaData();

            Field field = JdbcParameterMetaData.class.getDeclaredField("parameters");
            field.setAccessible(true);
            ArrayList<Parameter> params = (ArrayList) field.get(metaData);
            Long value = params.get(0).getParamValue().getLong();
            assertEquals(param, value);
        } catch (SQLException e) {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            } if(statement != null) {
                statement.close();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseCharacterParameterTest() throws SQLException {
        Optional<Connection> factory = DbConfigurationUtil.getTestConnection();
        Connection connection = factory.get();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM Book where author =?");
            Character param = Character.MAX_VALUE;
            SQLExecutor.parseParameters(statement, param);
            ParameterMetaData metaData = statement.getParameterMetaData();

            Field field = JdbcParameterMetaData.class.getDeclaredField("parameters");
            field.setAccessible(true);
            ArrayList<Parameter> params = (ArrayList) field.get(metaData);
            Character value = params.get(0).getParamValue().getString().charAt(0);
            assertEquals(param, value);
        } catch (SQLException e) {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            } if(statement != null) {
                statement.close();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseShortParameterTest() throws SQLException {
        Optional<Connection> factory = DbConfigurationUtil.getTestConnection();
        Connection connection = factory.get();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM Book where author =?");
            Short param = Short.MAX_VALUE;
            SQLExecutor.parseParameters(statement, param);
            ParameterMetaData metaData = statement.getParameterMetaData();

            Field field = JdbcParameterMetaData.class.getDeclaredField("parameters");
            field.setAccessible(true);
            ArrayList<Parameter> params = (ArrayList) field.get(metaData);
            Short value = params.get(0).getParamValue().getShort();
            assertEquals(param, value);
        } catch (SQLException e) {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            } if(statement != null) {
                statement.close();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseDateParameterTest() throws SQLException {
        Optional<Connection> factory = DbConfigurationUtil.getTestConnection();
        Connection connection = factory.get();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM Book where author =?");
            Date param = new Date(System.currentTimeMillis());
            SQLExecutor.parseParameters(statement, param);
            ParameterMetaData metaData = statement.getParameterMetaData();

            Field field = JdbcParameterMetaData.class.getDeclaredField("parameters");
            field.setAccessible(true);
            ArrayList<Parameter> params = (ArrayList) field.get(metaData);
            Date date  = params.get(0).getParamValue().getDate();
            assertEquals(param.getTime(), date.getTime());
        } catch (SQLException e) {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            } if(statement != null) {
                statement.close();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseByteParameterTest() throws SQLException {
        Optional<Connection> factory = DbConfigurationUtil.getTestConnection();
        Connection connection = factory.get();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM Book where author =?");
            Byte param = Byte.MAX_VALUE;
            SQLExecutor.parseParameters(statement, param);
            ParameterMetaData metaData = statement.getParameterMetaData();

            Field field = JdbcParameterMetaData.class.getDeclaredField("parameters");
            field.setAccessible(true);
            ArrayList<Parameter> params = (ArrayList) field.get(metaData);
            Byte value  = params.get(0).getParamValue().getByte();
            assertEquals(param, value);
        } catch (SQLException e) {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            } if(statement != null) {
                statement.close();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseParameterIgnoresNullParams() throws SQLException {
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        SQLExecutor.parseParameters(mockStatement, null, null);
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