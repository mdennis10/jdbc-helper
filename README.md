# jdbc-helper
A simple library to make working with JDBC drivers easier. JDBC Helper provides the following benefits:

- Very minimal configuration.
- DataSource management with connection pools.
- Object Relationship Mapping.
- Simple API to execute SQL commands.

## Usage
- Create DbConfig instance containing database configuration.
<pre>
DbConfig config = new DbConfig(
    "dbUser",
    "dbPassword",
    "jdbc:h2:file:~/dbName",
    "org.h2.Driver"
);
</pre>

- Instantiate instance of DatabaseHelper class used to execute database commands. JDBC Helper handles
resource cleanup and internally uses the HikariCP connection pool.
<pre>
DatabaseHelper databaseHelper = new DatabaseHelper(config);
String sql = "INSERT INTO Person(firstName, lastName) VALUES(?,?)"
int result = databaseHelper.executeUpdate(sql, new Object[]{"John", "Doe"});
</pre>

- JDBC Helper provides support for object relationship mapping to map entity classes.
<pre>
String sql = "SELECT NAME FROM Person WHERE firstName=?";
Optional<Person> result = databaseHelper.query(Person.class, sql, new Object[]{"JOHN"});
</pre>

- You can also use a ColumnMapper to map your entity classes.
<pre>
String sql = "SELECT NAME FROM Person WHERE fistName=?";
ColumnMapper<Person> mapper = row -> new Person(
      row.get("firstname"),
      row.get("lastname")
);
Optional<Person> result = databaseHelper.query(sql, new Object[]{"JOHN"}, mapper);
</pre>

