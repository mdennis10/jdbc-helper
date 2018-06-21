package com.dennis.jdbc.helper.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TypeData {
    private String columnName;
    private Class<?> fieldType;
    private String fieldName;
}
