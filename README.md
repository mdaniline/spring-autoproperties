# spring-autoproperties

[![Build Status](https://travis-ci.org/mdaniline/spring-autoproperties.svg?branch=master)](https://travis-ci.org/mdaniline/spring-autoproperties)

A Spring plugin to automatically generate implementations of interfaces using `@Value` annotations. This is handy for organising multiple related properties into a single beans without needing to implement the bean itself.

### Installation

Get the jar through Maven:

```xml
<dependency>
    <groupId>com.mdaniline</groupId>
    <artifactId>spring-autoproperties</artifactId>
    <version>1.0.1</version>
</dependency>
```

### Dependencies

* Java 1.6+
* Spring 4.2+

### Usage

1.  Annotate your application config bean with `@EnableAutoProperties`. You will need to specify the `basePackages` and/or `basePackageClasses` properties to configure which packages should be scanned.
2.  Create an interface which you want to proxy and annotate it with `@AutoProperties`. Every method should be in getter-style (non-void return value and no parameters) and annotated with `@Value`. For example:
```java
@AutoProperties
public interface DatabaseSettings {

    @Value("${db.connectionString}")
    String getConnectionString();

    @Value("${db.username}")
    String getUsername();

    @Value("${db.password}")
    String getPassword();

    @Value("${db.pool.maxActive}")
    Integer getMaxActiveConnections();
}
```

### Why would I bother?

You can now inject this single interface into your beans instead of a long, messy list of properties, e.g. 
```java
public class CustomDataSource {
    @Autowired
    public CustomDataSource(DatabaseSettings settings) {
        ...
    }
    ...
}
```
instead of
```java
public class CustomDataSource {
    @Autowired
    public CustomDataSource(@Value("${db.connectionString}") String connectionString,
                            @Value("${db.username}") String username,
                            @Value("${db.password}") String password,
                            @Value("${db.pool.maxActive}") Integer maxActiveConnections) {
        ...
    }
    ...
}
```



