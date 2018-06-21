package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.annotation.Column;
import com.dennis.jdbc.helper.annotation.TypeData;
import com.dennis.jdbc.helper.exception.NoColumnAnnotationException;
import com.dennis.jdbc.helper.util.RefStreamsUtil;
import com.google.common.base.Preconditions;
import java8.util.stream.Collectors;

import java.lang.reflect.Field;
import java.util.List;


public class AnnotationParser {
    protected static <T> List<TypeData> getColumnNames(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<TypeData> columns = RefStreamsUtil.createStream(fields)
                .filter(x -> x.getAnnotation(Column.class) != null)
                .map(x -> mapTypeData(x)).collect(Collectors.toList());
        return columns;
    }


    protected static TypeData mapTypeData(Field field) {
        Preconditions.checkNotNull(field);
        Column column = field.getAnnotation(Column.class);
        if (column == null)
            throw new NoColumnAnnotationException();
        return new TypeData(column.name(), field.getType(), field.getName());
    }
}
