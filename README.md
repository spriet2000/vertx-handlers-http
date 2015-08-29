# Vert.x handlers-http

Handlers-http provides a minimal and adaptable interface for developing web applications on the vert-x3 platform.

[![Build Status](https://travis-ci.org/spriet2000/vertx-handlers-http.svg?branch=master)](https://travis-ci.org/spriet2000/vertx-handlers-http)

## Example

```java 

        Handler<Throwable> exception = e -> {};
        Handler<Object> success = e -> {};

        RequestHandlers<HttpServerRequest> handlers =
                new RequestHandlers<>(exception, success);

        handlers.then(new ExceptionHandler(),
                new TimeOutHandler(vertx),
                new ResponseTimeHandler(),
                new EndHandler());

        server.requestHandler(handlers::handle)
                .listen();

```
### Custom context

```java 

        Handler<Throwable> exception = logger::error;
        Handler<Object> success = logger::info;

        RequestHandlers<HttpContext> handlers =
                new RequestHandlers<>(exception, success);

        handlers.then((f, n) -> e -> e.request().response().end());

        server.requestHandler(e -> handlers.handle(e, HttpContext::new))
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
