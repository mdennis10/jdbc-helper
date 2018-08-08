package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.annotation.Column;
import com.dennis.jdbc.helper.annotation.TypeData;
import com.dennis.jdbc.helper.exception.NoColumnAnnotationException;
import com.google.common.base.Preconditions;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


class AnnotationParser {
    private static final Logger LOGGER;
    static {
        LOGGER = Logger.getLogger(AnnotationParser.class.getName());
        LOGGER.setLevel(Level.WARNING);
    }

    static <T> List<TypeData> getColumnNames(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields)
                .filter(x -> x.getAnnotation(Column.class) != null)
                .map(AnnotationParser::mapTypeData).collect(Collectors.toList());
    }


    private static TypeData mapTypeData(Field field) {
        Preconditions.checkNotNull(field);
        Column column = field.getAnnotation(Column.class);
        if (column == null) {
            NoColumnAnnotationException exception = new NoColumnAnnotationException();
            LOGGER.severe(exception.getMessage());
            throw exception;
        }
        return new TypeData(column.name(), field.getType(), field.getName());
    }
}
