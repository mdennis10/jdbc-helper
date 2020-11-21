package com.jdbc.helper;

import com.jdbc.helper.model.Person;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReflectiveTypeResolverTest {

    @Test void resolve() {
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("name", "John Doe");
        dataResult.put("age", 18);
        dataResult.put("dateOfBirth", LocalDateTime.now());
        dataResult.put("isActive", true);
        Person person = ReflectiveTypeResolver.resolve(Person.class, dataResult);
        assertNotNull(person);
        assertEquals(person.getAge(), dataResult.get("age"));
        assertEquals(person.getName(), dataResult.get("name"));
        assertEquals(person.isActive(), dataResult.get("isActive"));
        assertEquals(person.getDateOfBirth(), dataResult.get("dateOfBirth"));
    }

    @Test void result_InvalidArguments() {
        // assert null type
        NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> ReflectiveTypeResolver.resolve(null, new HashMap<>())
        );
        assertEquals(exception.getMessage(), "null clazz supplied");

        //assert null dataResult
        exception = assertThrows(
            NullPointerException.class,
            () -> ReflectiveTypeResolver.resolve(Person.class, null)
        );
        assertEquals(exception.getMessage(), "null dataResult supplied");

        //assert empty dataResult
        Person result = ReflectiveTypeResolver.resolve(Person.class, new HashMap<>());
        assertNull(result);
    }

    @Test void resolve_dataResultKeyDoesNotExist() {
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("name", "John Doe");
        Person person = ReflectiveTypeResolver.resolve(Person.class, dataResult);
        assertNotNull(person);
        assertEquals(person.getName(), dataResult.get("name"));
        assertNull(person.getDateOfBirth());
        assertFalse(person.isActive());
        assertEquals(0, person.getAge());
    }

    @Test void resolve_dataResultKeyNameCaseSensitivity() {
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("NAME", "John Doe");
        dataResult.put("AGE", 18);
        dataResult.put("dateofbirth", LocalDateTime.now());
        dataResult.put("isactive", true);
        Person person = ReflectiveTypeResolver.resolve(Person.class, dataResult);
        assertNotNull(person);
        assertEquals(person.getAge(), dataResult.get("AGE"));
        assertEquals(person.getName(), dataResult.get("NAME"));
        assertEquals(person.isActive(), dataResult.get("isactive"));
        assertEquals(person.getDateOfBirth(), dataResult.get("dateofbirth"));
    }

    @Test void resolve_cacheTest() {
        Map<String, Object> dataResult = new HashMap<>();
        dataResult.put("name", "John Doe");
        dataResult.put("age", 18);
        dataResult.put("dateOfBirth", LocalDateTime.now());
        dataResult.put("isActive", true);
    }
}