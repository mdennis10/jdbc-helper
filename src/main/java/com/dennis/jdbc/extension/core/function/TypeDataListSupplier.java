package com.dennis.jdbc.extension.core.function;

import com.dennis.jdbc.extension.core.annotation.TypeData;
import java8.util.function.Supplier;

import java.util.ArrayList;
import java.util.List;

public class TypeDataListSupplier implements Supplier<List<TypeData>> {
    public List<TypeData> get() {
        return new ArrayList<TypeData>();
    }
}
