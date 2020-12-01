# jdbc-helper
A simple library to make working with JDBC drivers easier. JDBC Helper provides the following benefits:

- Very minimal configuration.
- DataSource management with connection pools.
- Object Relationship Mapping.
- Simple API to execute SQL commands.

## Usage
- Create a DbConfig instance containing database configuration.
```java
DbConfig config = new DbConfig(
    "dbUser",
    "dbPassword",
    "jdbc:h2:file:~/dbName",
    "org.h2.Driver"
);
```

- A instance of the DatabaseHelper class is used to execute database commands.
```java
DatabaseHelper databaseHelper = new DatabaseHelper(config);
String sql = "INSERT INTO Person(firstName, lastName) VALUES(?,?)"
int result = databaseHelper.executeUpdate(sql, new Object[]{"John", "Doe"});
```

- JDBC Helper provides support for object relationship mapping to entity classes.
```java
String sql = "SELECT * FROM Person WHERE firstName=?";
Optional<Person> result = databaseHelper.query(Person.class, sql, new Object[]{"John"});`
```

- You can also use a ColumnMapper to map your entity classes.
```java
String sql = "SELECT * FROM Person WHERE fistName=?";
ColumnMapper<Person> mapper = row -> new Person(
      row.get("firstname"),
      row.get("lastname")
);
Optional<Person> result = databaseHelper.query(sql, new Object[]{"JOHN"}, mapper);
List<Person> persons = databaseHelper.queryForList(Person.class, "SELECT * FROM Person", new Object[]{});
```
- JDBC Helper uses Hikari connection pool internally, therefore resource cleanup is necessary on application shutdown.
```java
databaseHelper.close() // should only be called on application shutdown

```

