package com.dennis.jdbc.extension.core;

import com.dennis.jdbc.extension.core.annotation.TypeData;
import com.google.common.base.Strings;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AnnotationParserTest {
    @Test
    public void getColumnNamesTest() {
        List<TypeData> result = AnnotationParser.getColumnNames(Book.class);
        assertNotNull(result);
        assertFalse(result.isEmpty());

        TypeData data = result.get(0);
        assertTrue(!Strings.isNullOrEmpty(data.getColumnName()));
        assertTrue(data.getFieldType() == String.class);
        assertEquals("author", data.getFieldName());
    }
}
