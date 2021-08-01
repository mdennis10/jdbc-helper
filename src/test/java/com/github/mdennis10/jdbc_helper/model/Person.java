package com.github.mdennis10.jdbc_helper.model;

import java.time.LocalDateTime;

public class Person {
    private String name;
    private int age;
    private LocalDateTime dateOfBirth;
    private boolean isActive;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

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
