package com.dennis.jdbc.helper.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
public class DbConfiguration {
    @Setter(AccessLevel.NONE)
    private String username;

    @Setter(AccessLevel.NONE)
    private String password;

    @Setter(AccessLevel.NONE)
    private String url;

    @Setter(AccessLevel.NONE)
    private String driverClassName;

    @Setter(AccessLevel.NONE)
    private int maxPoolSize;

    @Setter(AccessLevel.NONE)
    private int minPoolSize;
}
