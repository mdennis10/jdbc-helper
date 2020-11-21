package com.jdbc.helper;

import com.google.common.base.Preconditions;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;

public final class ReflectiveTypeResolver {
    private ReflectiveTypeResolver () {}

    protected static <T> T resolve(@NotNull Class<T> clazz, @NotNull Map<String, Object> dataResult) {
        Preconditions.checkNotNull(clazz, "null clazz supplied");
        Preconditions.checkNotNull(dataResult,"null dataResult supplied");
        if(dataResult.isEmpty()) {
            return null;
        }
        Map<String, Object> newDataResult = dataResult
                .keySet()
                .stream()
                .collect(Collectors.toMap(String::toUpperCase, dataResult::get));
        try {
            T newInstance = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields) {
                String fieldName = field.getName().toUpperCase();
                if(newDataResult.containsKey(fieldName)) {
                    field.setAccessible(true);
                    field.set(newInstance, newDataResult.get(fieldName));
                }
            }
            return newInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new UnsupportedOperationException();
    }
}
