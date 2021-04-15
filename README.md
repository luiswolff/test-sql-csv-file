# Test SQL to CSV

This project shows how to convert the result set of a SQL command to a CSV file.

## Usage

The main logic is encapsulated in the class `SqlToCsvConverter`.
It accepts the following properties:

 * **jdbcUrl**
 * **jdbcUser**
 * **jdbcPassword**
 * **sqlQuery**
 * **targetFile**

With all that properties the method `SqlToCsvConverter#convert` can be called.
It opens a new SQL connection using the url and credentials, executes the provided query and writes it as CSV to the target file.

A sample execution is provided in the class `SqlToCsvConverterTest` from the test scope.
In order to check whether it works just type the following command:

```
$ mvn test
```

## Dependencies

The projects uses the following Maven dependencies:

 * **org.apache.commons:commons-csv:1.8 - compile**: A Java library for writing CSV data.
 * **com.h2database:h2:1.4.200 - test**: JDBC driver that can provide a In-Memory database that is initialized by the test driver. 
 * **junit:junit:4.13.1 - test**: Test driver Framework