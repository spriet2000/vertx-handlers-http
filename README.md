# Vert.x handlers-http

Handlers-http provides a minimal and adaptable interface for developing web applications on the vert-x3 platform.

[![Build Status](https://travis-ci.org/spriet2000/vertx-handlers-http.svg?branch=master)](https://travis-ci.org/spriet2000/vertx-handlers-http)

## Example

```java 
    
Handlers<HttpServerRequest, Void> handlers = new Handlers<>(
        new ExceptionHandler(),
        new ResponseTimeHandler(),
        new TimeOutHandler(vertx),
        new HelloWorldHandler());

server.requestHandler(e -> handlers.accept(e, null,
        (e, a) -> logger.error(a),
        (e, a) -> logger.info(a)))
            .listen();

```
### Example handler

```java

public class HelloWorldHandler<T> implements BiFunction<Consumer<Throwable>, Consumer<Object>, 
                                                            BiConsumer<HttpServerRequest, T>> {

    @Override   
    public BiConsumer<HttpServerRequest, T> apply(Consumer<Throwable> fail, Consumer<Object> next) {
        return (req, arg) -> {
            req.response().end("hello world!");
            next.accept(arg);
        };
    }
}

```

## Installation

### Maven

```xml

<dependency>
    <groupId>com.github.spriet2000</groupId>
    <artifactId>vertx-handlers</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>com.github.spriet2000</groupId>
    <artifactId>vertx-handlers-http</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>

```

### Without maven

[https://oss.sonatype.org/content/repositories/snapshots/com/github/spriet2000](https://oss.sonatype.org/content/repositories/snapshots/com/github/spriet2000)
