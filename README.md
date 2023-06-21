# jdbc-helper
A simple library that makes working with JDBC drivers easier. JDBC Helper provides the following benefits:

- Very minimal configuration: JDBC Helper requires very little configuration to get started. You can simply provide the JDBC driver class name and the database connection URL.
- Data source management with connection pools: JDBC Helper can automatically manage data source connections using connection pools. This can help to improve performance and scalability.
- Entity mapping: JDBC Helper can map Java objects to database tables. 
- Simple API to execute SQL commands: JDBC Helper provides a simple API to execute SQL commands. This API makes it easy to execute queries, insert data, and update data.
- It is lightweight and easy to use.
- It is thread-safe.

### Gradle
````
repositories {
    mavenCentral()
}

dependencies {
    compile 'com.github.mdennis10:jdbc-helper:1.0.2'
}
````
### Maven
```
<dependency>
    <groupId>com.github.mdennis10</groupId>
    <artifactId>jdbc-helper</artifactId>
    <version>1.0.2</version>
</dependency>
```
## Usage
Create a new DbConfig instance with database configuration.

```java
DbConfig config = new DbConfig(
    "dbUser",
    "dbPassword",
    "jdbc:h2:file:~/dbName",
    "org.h2.Driver"
);
```

An instance of the `DatabaseHelper` class is used to execute database commands.
```java
DatabaseHelper databaseHelper = new DatabaseHelper(config);
String sql = "INSERT INTO Person(firstName, lastName) VALUES(?,?)"
int result = databaseHelper.executeUpdate(sql, new Object[]{"John", "Doe"});
```

JDBC Helper provides object-relational mapping (ORM) support for entity classes.
```java
String sql = "SELECT * FROM Person WHERE firstName=?";
Optional<Person> result = databaseHelper.query(Person.class, sql, new Object[]{"John"});`
```

You can also use a column mapper to map your entity classes.
```java
String sql = "SELECT * FROM Person WHERE fistName=?";
ColumnMapper<Person> mapper = row -> new Person(
      row.get("firstname"),
      row.get("lastname")
);
Optional<Person> result = databaseHelper.query(sql, new Object[]{"JOHN"}, mapper);
List<Person> persons = databaseHelper.queryForList(Person.class, "SELECT * FROM Person", new Object[]{});
```
JDBC Helper uses HikariCP, an open-source connection pool, internally. Therefore, it is necessary to clean up resources on application shutdown.
```java
DatabaseHelper.close() // should only be called on application shutdown

```

