package com.github.mdennis10.jdbc_helper;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.annotation.Nullable;
import java.util.Objects;

public class DbConfig {
    private final String user;
    private final String password;
    private final String url;
    private final String driverClassName;

    public DbConfig(String user, @Nullable String password, String url, String driverClassName) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(user), "user is null or empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "jdbc url is null or empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(driverClassName), "driverClassName is null or empty");
        this.user = user;
        this.password = password;
        this.url = url;
        this.driverClassName = driverClassName;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbConfig config = (DbConfig) o;
        return getUser().equals(config.getUser()) &&
                getPassword().equals(config.getPassword()) &&
                getUrl().equals(config.getUrl()) &&
                getDriverClassName().equals(config.getDriverClassName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getPassword(), getUrl(), getDriverClassName());
    }
}
