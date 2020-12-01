package com.github.mdennis10.jdbc_helper.model;

import java.time.LocalDateTime;

public class Person {
    private String name;
    private int age;
    private LocalDateTime dateOfBirth;
    private boolean isActive;


    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public boolean isActive() {
        return isActive;
    }
}
