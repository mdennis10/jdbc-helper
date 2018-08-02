package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.annotation.TypeData;
import com.dennis.jdbc.helper.exception.ConnectionException;
import com.dennis.jdbc.helper.exception.HelperSqlException;
import com.dennis.jdbc.helper.exception.NoColumnAnnotationException;
import com.dennis.jdbc.helper.exception.UnsupportedTypeException;
import com.dennis.jdbc.helper.util.ConnectionUtil;
import com.dennis.jdbc.helper.util.RefStreamsUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AbstractDatabaseHelper {
    protected AbstractDatabaseHelper() { }

    protected <T> String convertParamToString(Class<T> clazz, T entity, TypeData typeData) {
        try {
            Field field = clazz.getDeclaredField(typeData.getFieldName());
            field.setAccessible(true);

            if (field.getType() == String.class) {
                return (String) field.get(entity);
            } else if (field.getType() == int.class) {
                return Integer.toString(field.getInt(entity));
            } else if (field.getType() == long.class) {
                return Long.toString(field.getLong(entity));
            } else if (field.getType() == double.class) {
                return Double.toString(field.getDouble(entity));
            } else if (field.getType() == char.class) {
                return Character.toString(field.getChar(entity));
            } else if (field.getType() == float.class) {
                return Float.toString(field.getFloat(entity));
            } else if (field.getType() == boolean.class) {
                boolean value = field.getBoolean(entity);
                return value ? "1" : "0";
            } else if (field.getType() == Boolean.class) {
                Boolean value = (Boolean) field.get(entity);
                return value ? "1" : "0";
            } else if (field.getType() == short.class) {
                return Short.toString(field.getShort(entity));
            } else if (field.getType() == byte.class) {
                return Byte.toString(field.getByte(entity));
            } else if (field.getType() == Character.class ||
                    field.getType() == Double.class ||
                    field.getType() == Integer.class ||
                    field.getType() == Long.class ||
                    field.getType() == Float.class ||
                    field.getType() == Short.class ||
                    field.getType() == Byte.class) {
                return field.get(entity).toString();
            } else {
                throw new UnsupportedTypeException(field.getType());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void close(ExecutionResult executionResult) {
        if (executionResult == null)
            return;
        try {
            if (executionResult.getConnection() != null && !executionResult.getConnection().isClosed())
                executionResult.getConnection().close();
            if (executionResult.getPreparedStatement() != null)
                executionResult.getPreparedStatement().close();
            if (executionResult.getResultSet() != null)
                executionResult.getResultSet().close();
        } catch (SQLException e) {
            throw new HelperSqlException(e);
        }
    }

    protected void close(UpdateExecutionResult executionResult) {
        if(executionResult == null) {
            return;
        }
        ExecutionResult result = new ExecutionResult(
            executionResult.getConnection(),
            executionResult.getPreparedStatement(),
            executionResult.getResultSet()
        );
        close(result);
    }

    protected <T> List<T> parseEntity(final ResultSet resultSet, Class<T> clazz) {
        if (resultSet == null)
            return new ArrayList<T>();
        if (ConnectionUtil.isClosed(resultSet))
            throw new ConnectionException();
        List<T> result = new ArrayList<T>();
        List<TypeData> typeDataList = AnnotationParser.getColumnNames(clazz);
        // likely no column annotation defined
        // therefore unable to parse entity
        if (typeDataList.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append(clazz.getSimpleName());
            builder.append(" does not contain any Column annotation");
            throw new NoColumnAnnotationException(builder.toString());
        }
        try {
            while (resultSet.next()) {
                final T entity = clazz.newInstance();
                RefStreamsUtil
                        .createStream(typeDataList)
                        .forEach(typeData -> setField(resultSet, entity, typeData));
                result.add(entity);
            }
            return result;
        } catch (SQLException e) {
            throw new HelperSqlException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> void setField(ResultSet resultSet, T entity, TypeData typeData) {
        Preconditions.checkNotNull(resultSet);
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(typeData);
        try {
            Field field = entity.getClass().getDeclaredField(typeData.getFieldName());
            field.setAccessible(true);
            setValue(field, resultSet, entity, typeData);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new HelperSqlException(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private <T> void setValue(Field field, ResultSet resultSet, T entity, TypeData typeData) throws NoSuchFieldException, IllegalAccessException, SQLException {
        if (typeData.getFieldType() == String.class) {
            String value = resultSet.getString(typeData.getColumnName());
            field.set(entity, value);
        } else if (typeData.getFieldType() == int.class || typeData.getFieldType() == Integer.class) {
            int value = resultSet.getInt(typeData.getColumnName());
            field.set(entity, value);
        } else if (typeData.getFieldType() == double.class || typeData.getFieldType() == Double.class) {
            double value = resultSet.getDouble(typeData.getColumnName());
            field.set(entity, value);
        } else if (typeData.getFieldType() == float.class || typeData.getFieldType() == Float.class) {
            float value = resultSet.getFloat(typeData.getColumnName());
            field.set(entity, value);
        } else if (typeData.getFieldType() == long.class || typeData.getFieldType() == Long.class) {
            long value = resultSet.getLong(typeData.getColumnName());
            field.set(entity, value);
        } else if (typeData.getFieldType() == char.class || typeData.getFieldType() == Character.class) {
            String dbValue = resultSet.getString(typeData.getColumnName());
            if (Strings.isNullOrEmpty(dbValue))
                return;
            field.set(entity, dbValue.charAt(0));
        } else if (typeData.getFieldType() == short.class || typeData.getFieldType() == Short.class) {
            short value = resultSet.getShort(typeData.getColumnName());
            field.set(entity, value);
        } else if (typeData.getFieldType() == boolean.class || typeData.getFieldType() == Boolean.class) {
            boolean value = resultSet.getBoolean(typeData.getColumnName());
            field.set(entity, value);
        } else if (typeData.getFieldType() == byte.class || typeData.getFieldType() == Byte.class) {
            byte value = resultSet.getByte(typeData.getColumnName());
            field.set(entity, value);
        } else if(typeData.getFieldType() == Date.class) {
            java.sql.Date date = resultSet.getDate(typeData.getColumnName());
            field.set(entity, new Date(date.getTime()));
        } else if(typeData.getFieldType() == java.sql.Date.class) {
            java.sql.Date date = resultSet.getDate(typeData.getColumnName());
            field.set(entity, date);
        }else {
            throw new UnsupportedTypeException(typeData.getFieldType());
        }
    }
}
