package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.annotation.Column;
import com.dennis.jdbc.helper.annotation.TypeData;
import com.dennis.jdbc.helper.exception.UnsupportedTypeException;
import com.google.common.base.Strings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DatabaseHelper_SupportedDataTypesTest {
    @Test
    public void setFieldDoubleDataTypeTest() throws SQLException {
        SetDoubleFieldEntity entity = new SetDoubleFieldEntity();

        double expected = Double.MAX_VALUE;
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, double.class, "doubleField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getDouble(columnName)).thenReturn(expected);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.doubleField, 0.01);
    }



    @Test
    public void setFieldDoubleWrapperDataTypeTest() throws SQLException {
        SetDoubleWrapperFieldEntity entity = new SetDoubleWrapperFieldEntity();

        double expected = Double.MAX_VALUE;
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, Double.class, "doubleField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getDouble(columnName)).thenReturn(expected);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.doubleField.doubleValue(), 0.01);
    }

    @Test
    public void setFieldStringDataTypeTest() throws SQLException {
        SetStringFieldEntity entity = new SetStringFieldEntity();

        String expected = "some-value";
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, String.class, "stringField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getString(columnName)).thenReturn(expected);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.stringField);
    }

    @Test
    public void setFieldCharDataTypeTest() throws SQLException {
        SetCharFieldEntity entity = new SetCharFieldEntity();

        char expected = 'a';
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, char.class, "charField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getString(columnName)).thenReturn("abc");

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.charField);
    }

    @Test
    public void setFieldCharacterDataTypeTest() throws SQLException {
        SetCharacterFieldEntity entity = new SetCharacterFieldEntity();

        char expected = 'a';
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, Character.class, "characterField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getString(columnName)).thenReturn("abc");

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.characterField.charValue());
    }


    @Test
    public void setFieldCharacterDataTypeNullTest() throws SQLException {
        SetCharacterFieldEntity entity = new SetCharacterFieldEntity();

        String columnName = "name";
        TypeData typeData = new TypeData(columnName, Character.class, "characterField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getString(columnName)).thenReturn(null);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertNull(entity.characterField);
    }


    @Test
    public void setFieldIntegerWrapperDataTypeTest() throws SQLException {
        SetIntegerWrapperEntity entity = new SetIntegerWrapperEntity();

        int expected = Integer.MAX_VALUE;
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, Integer.class, "integerField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getInt(columnName)).thenReturn(expected);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.integerField.intValue());
    }

    @Test
    public void setFieldIntDataTypeTest() throws SQLException {
        SetIntFieldEntity entity = new SetIntFieldEntity();

        int expected = Integer.MAX_VALUE;
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, int.class, "intField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getInt(columnName)).thenReturn(expected);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.intField);
    }

    @Test
    public void setFieldFloatDataTypeTest() throws SQLException {
        SetFloatFieldEntity entity = new SetFloatFieldEntity();

        float expected = Float.MAX_VALUE;
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, float.class, "floatField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getFloat(columnName)).thenReturn(expected);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.floatField, 0.01);
    }

    @Test
    public void setFieldFloatWrapperDataTypeTest() throws SQLException {
        SetFloatWrapperEntity entity = new SetFloatWrapperEntity();

        float expected = Float.MAX_VALUE;
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, Float.class, "floatField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getFloat(columnName)).thenReturn(expected);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.floatField.doubleValue(), 0.01);
    }


    @Test
    public void setFieldLongDataTypeTest() throws SQLException {
        SetLongFieldEntity entity = new SetLongFieldEntity();

        long expected = Long.MAX_VALUE;
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, long.class, "longField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getLong(columnName)).thenReturn(expected);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.longField);
    }

    @Test
    public void setFieldLongWrapperDataTypeTest() throws SQLException {
        SetLongWrapperFieldEntity entity = new SetLongWrapperFieldEntity();

        long expected = Long.MAX_VALUE;
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, Long.class, "longField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getLong(columnName)).thenReturn(expected);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.longField.longValue());
    }

    @Test
    public void setFieldShortDataTypeTest() throws SQLException {
        SetShortFieldEntity entity = new SetShortFieldEntity();

        short expected = Short.MAX_VALUE;
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, short.class, "shortField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getShort(columnName)).thenReturn(expected);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.shortField);
    }

    @Test
    public void setFieldShortWrapperDataTypeTest() throws SQLException {
        SetShortWrapperFieldEntity entity = new SetShortWrapperFieldEntity();

        short expected = Short.MAX_VALUE;
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, Short.class, "shortField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getShort(columnName)).thenReturn(expected);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.shortField.shortValue());
    }

    @Test
    public void setFieldBooleanDataTypeTest() throws SQLException {
        SetBooleanFieldEntity entity = new SetBooleanFieldEntity();

        boolean expected = Boolean.TRUE;
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, boolean.class, "booleanField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getBoolean(columnName)).thenReturn(expected);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.booleanField);
    }

    @Test
    public void setFieldBooleanWrapperDataTypeTest() throws SQLException {
        SetBooleanWrapperFieldEntity entity = new SetBooleanWrapperFieldEntity();

        boolean expected = Boolean.TRUE;
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, Boolean.class, "booleanField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getBoolean(columnName)).thenReturn(expected);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.booleanField.booleanValue());
    }

    @Test
    public void setFieldThrowsUnsupportedTestException() {
        UnsupportedTypeEntity entity = new UnsupportedTypeEntity();
        entity.book = new Book();

        TypeData typeData = new TypeData("name", Book.class, "book");
        DatabaseHelper helper = DatabaseHelper.getInstance();

        Executable executable = () -> helper.setField(mock(ResultSet.class), entity, typeData);
        assertThrows(UnsupportedTypeException.class, executable);
    }

    @Test
    public void setFieldByteDataTypeTest() throws SQLException {
        SetByteFieldEntity entity = new SetByteFieldEntity();

        byte expected = Byte.MAX_VALUE;
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, byte.class, "byteField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getByte(columnName)).thenReturn(expected);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.byteField);
    }

    @Test
    public void setFieldByteWrapperDataTypeTest() throws SQLException {
        SetByteWrapperFieldEntity entity = new SetByteWrapperFieldEntity();

        byte expected = Byte.MAX_VALUE;
        String columnName = "name";
        TypeData typeData = new TypeData(columnName, Byte.class, "byteField");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getByte(columnName)).thenReturn(expected);

        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.byteField.byteValue());
    }

    @Test
    public void convertParamToStringStringDataTypeTest() {
        SetStringFieldEntity entity = new SetStringFieldEntity();

        entity.stringField = "some-value";
        TypeData typeData = new TypeData("name", String.class, "stringField");

        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetStringFieldEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals(entity.stringField, result);
    }

    @Test
    public void convertParamToStringIntDataTypeTest() {
        SetIntFieldEntity entity = new SetIntFieldEntity();
        entity.intField = Integer.MAX_VALUE;

        TypeData typeData = new TypeData("name", int.class, "intField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetIntFieldEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals(Integer.toString(entity.intField), result);
    }

    @Test
    public void convertParamToStringIntegerDataTypeTest() {
        SetIntegerWrapperEntity entity = new SetIntegerWrapperEntity();
        entity.integerField = Integer.MAX_VALUE;

        TypeData typeData = new TypeData("name", Integer.class, "integerField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetIntegerWrapperEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals(Integer.toString(entity.integerField), result);
    }

    @Test

    public void convertParamToStringLongDataTypeTest() {
        SetLongFieldEntity entity = new SetLongFieldEntity();
        entity.longField = Long.MAX_VALUE;

        TypeData typeData = new TypeData("name", long.class, "longField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetLongFieldEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals(Long.toString(entity.longField), result);
    }

    @Test
    public void convertParamToStringLongWrapperDataTypeTest() {
        SetLongWrapperFieldEntity entity = new SetLongWrapperFieldEntity();
        entity.longField = Long.MAX_VALUE;

        TypeData typeData = new TypeData("name", Long.class, "longField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetLongWrapperFieldEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals(Long.toString(entity.longField), result);
    }

    @Test
    public void convertParamToStringDoubleDataTypeTest() {
        SetDoubleFieldEntity entity = new SetDoubleFieldEntity();
        entity.doubleField = Double.MAX_VALUE;

        TypeData typeData = new TypeData("name", double.class, "doubleField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetDoubleFieldEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals(Double.toString(entity.doubleField), result);
    }

    @Test
    public void convertParamToStringDoubleWrapperDataTypeTest() {
        SetDoubleWrapperFieldEntity entity = new SetDoubleWrapperFieldEntity();
        entity.doubleField = Double.MAX_VALUE;

        TypeData typeData = new TypeData("name", Double.class, "doubleField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetDoubleWrapperFieldEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals(Double.toString(entity.doubleField), result);
    }

    @Test
    public void convertParamToStringCharDataTypeTest() {
        SetCharFieldEntity entity = new SetCharFieldEntity();
        entity.charField = Character.MAX_VALUE;

        TypeData typeData = new TypeData("name", char.class, "charField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetCharFieldEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals(Character.toString(entity.charField), result);
    }

    @Test
    public void convertParamToStringCharacterDataTypeTest() {
        SetCharacterFieldEntity entity = new SetCharacterFieldEntity();
        entity.characterField = Character.MAX_VALUE;

        TypeData typeData = new TypeData("name", Character.class, "characterField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetCharacterFieldEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals(Character.toString(entity.characterField), result);
    }

    @Test
    public void convertParamToStringFloatDataTypeTest() {
        SetFloatFieldEntity entity = new SetFloatFieldEntity();
        entity.floatField = Float.MAX_VALUE;

        TypeData typeData = new TypeData("name", float.class, "floatField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetFloatFieldEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals(Float.toString(entity.floatField), result);
    }

    @Test
    public void convertParamToStringFloatWrapperDataTypeTest() {
        SetFloatWrapperEntity entity = new SetFloatWrapperEntity();
        entity.floatField = Float.MAX_VALUE;

        TypeData typeData = new TypeData("name", Float.class, "floatField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetFloatWrapperEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals(Float.toString(entity.floatField), result);
    }

    @Test
    public void convertParamToStringBooleanDataTypeTest() {
        SetBooleanFieldEntity entity = new SetBooleanFieldEntity();
        entity.booleanField = Boolean.TRUE;

        TypeData typeData = new TypeData("name", boolean.class, "booleanField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetBooleanFieldEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals("1", result);
    }

    @Test
    public void convertParamToStringBooleanWrapperDataTypeTest() {
        SetBooleanWrapperFieldEntity entity = new SetBooleanWrapperFieldEntity();
        entity.booleanField = Boolean.TRUE;

        TypeData typeData = new TypeData("name", Boolean.class, "booleanField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetBooleanWrapperFieldEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals("1", result);
    }

    @Test
    public void convertParamToStringShortDataTypeTest() {
        SetShortFieldEntity entity = new SetShortFieldEntity();
        entity.shortField = Short.MAX_VALUE;

        TypeData typeData = new TypeData("name", Short.class, "shortField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetShortFieldEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals("32767", result);
    }

    @Test
    public void convertParamToStringShortWrapperDataTypeTest() {
        SetShortWrapperFieldEntity entity = new SetShortWrapperFieldEntity();
        entity.shortField = Short.MAX_VALUE;

        TypeData typeData = new TypeData("name", Short.class, "shortField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetShortWrapperFieldEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals("32767", result);
    }

    @Test
    public void convertParamToStringByteDataTypeTest() {
        SetByteFieldEntity entity = new SetByteFieldEntity();
        entity.byteField = Byte.MAX_VALUE;

        TypeData typeData = new TypeData("name", byte.class, "byteField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetByteFieldEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals("127", result);
    }

    @Test
    public void convertParamToStringByteWrapperDataTypeTest() {
        SetByteWrapperFieldEntity entity = new SetByteWrapperFieldEntity();
        entity.byteField = Byte.MAX_VALUE;

        TypeData typeData = new TypeData("name", Byte.class, "byteField");
        DatabaseHelper helper = DatabaseHelper.getInstance();
        String result = helper.convertParamToString(SetByteWrapperFieldEntity.class, entity, typeData);
        assertFalse(Strings.isNullOrEmpty(result));
        assertEquals("127", result);
    }

    @Test
    public void convertParamToStringUnsupportedTypeTest() {
        final UnsupportedTypeEntity entity = new UnsupportedTypeEntity();
        entity.book = new Book();

        final TypeData typeData = new TypeData("name", Book.class, "book");
        final DatabaseHelper helper = DatabaseHelper.getInstance();

        Executable executable = () -> helper.convertParamToString(UnsupportedTypeEntity.class, entity, typeData);
        assertThrows(UnsupportedTypeException.class, executable);
    }

    @Test
    public void setFieldDateDateType() throws SQLException {
        SetDateFieldEntity entity = new SetDateFieldEntity();
        Date expected = new Date();

        String columnName = "name";
        TypeData typeData = new TypeData(columnName, Date.class, "date");
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getDate(columnName)).thenReturn(new java.sql.Date(expected.getTime()));


        DatabaseHelper.getInstance().setField(mockResultSet, entity, typeData);
        assertEquals(expected, entity.date);
    }

    private class SetByteWrapperFieldEntity {
        @Column(name = "name")
        private Byte byteField;
    }

    private class SetByteFieldEntity {
        @Column(name = "name")
        private byte byteField;
    }

    private class SetBooleanWrapperFieldEntity {
        @Column(name = "name")
        private Boolean booleanField;
    }

    private class SetBooleanFieldEntity {
        @Column(name = "name")
        private boolean booleanField;
    }

    private class SetShortWrapperFieldEntity {
        @Column(name = "name")
        private Short shortField;
    }

    private class SetShortFieldEntity {
        @Column(name = "name")
        private short shortField;
    }

    private class SetLongWrapperFieldEntity {
        @Column(name = "name")
        private Long longField;
    }

    private class SetLongFieldEntity {
        @Column(name = "name")
        private long longField;
    }

    private class SetIntFieldEntity {
        @Column(name = "name")
        private int intField;
    }

    private class SetIntegerWrapperEntity {
        @Column(name = "name")
        private Integer integerField;
    }

    private class SetFloatWrapperEntity {
        @Column(name = "name")
        private Float floatField;
    }

    private class SetFloatFieldEntity {
        @Column(name = "name")
        private float floatField;
    }

    private class SetCharFieldEntity {
        @Column(name = "name")
        private char charField;
    }

    private class SetCharacterFieldEntity {
        @Column(name = "name")
        private Character characterField;
    }

    private class SetStringFieldEntity {
        @Column(name = "name")
        private String stringField;
    }

    private class SetDoubleFieldEntity {
        @Column(name = "name")
        private double doubleField;
    }

    private class SetDoubleWrapperFieldEntity {
        @Column(name = "name")
        private Double doubleField;
    }

    private class UnsupportedTypeEntity {
        @Column(name = "name")
        private Book book;
    }

    private class SetDateFieldEntity {
        @Column(name = "name")
        private Date date;
    }
}
