package com.dennis.jdbc.extension.core;

import com.dennis.jdbc.extension.core.annotation.Column;
import com.dennis.jdbc.extension.core.annotation.Table;
import com.dennis.jdbc.extension.core.annotation.TypeData;
import com.dennis.jdbc.extension.core.exception.NoColumnAnnotationException;
import com.dennis.jdbc.extension.core.util.RefStreamsUtil;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
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

    protected static <T> Optional<String> getTableName(Class<T> clazz) {
        Preconditions.checkArgument(clazz != null, "Class type not supplied");
        Table table = clazz.getAnnotation(Table.class);
        if (table == null || Strings.isNullOrEmpty(table.name())) {
            return Optional.absent();
        }
        return Optional.of(table.name());
    }

    protected static TypeData mapTypeData(Field field) {
        Preconditions.checkNotNull(field);
        Column column = field.getAnnotation(Column.class);
        if (column == null)
            throw new NoColumnAnnotationException();
        return new TypeData(column.name(), field.getType(), field.getName());
    }
}
