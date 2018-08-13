package com.dennis.jdbc.helper;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DatabaseHelper_GetInstanceTest {
    private final String DEFAULT_URL = "jdbc:h2:file:~/test";
    private final String MY_PROFILE_URL = "jdbc:h2:file:~/myprofile";

    @Test public void getInstance_utilizeTheCorrectDbConnectionProfileTest () {
        DatabaseHelper helper = DatabaseHelper.getInstance("myprofile");
        assertEquals(MY_PROFILE_URL, helper.getJdbcUrl());

        assertEquals(DEFAULT_URL, helper.getInstance().getJdbcUrl());
    }
}
