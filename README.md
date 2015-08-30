# Vert.x handlers-http

Handlers-http provides a minimal and adaptable interface for developing web applications on the vert-x3 platform.

[![Build Status](https://travis-ci.org/spriet2000/vertx-handlers-http.svg?branch=master)](https://travis-ci.org/spriet2000/vertx-handlers-http)

## Example

```java 
    
BiConsumer<HttpServerRequest, Throwable> exception = (e, a) -> logger.error(a);
BiConsumer<HttpServerRequest, Object> success = (e, a) -> logger.info(a);

RequestHandlers<HttpServerRequest> handlers =
        new RequestHandlers<>(exception, success);

handlers.andThen(new ExceptionHandler(),
        new ResponseTimeHandler(),
        new TimeOutHandler(vertx),
        new EndHandler());

server.requestHandler(e -> handlers.handle(e, null))
        .listen();

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
