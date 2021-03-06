# Vert.x handlers-http

Handlers-http provides a minimal and adaptable interface for developing web applications on the vert-x3 platform.

Handlers-http is an open webframework for the vert-x3 platform. The middleware layer doesn't depend on any custom classes, so components should be easily inter-changeable with different webframeworks.

Inspired by ['Build You Own Web Framework In Go'](https://www.nicolasmerouze.com/build-web-framework-golang)

[![Build Status](https://travis-ci.org/spriet2000/vertx-handlers-http.svg?branch=master)](https://travis-ci.org/spriet2000/vertx-handlers-http)

## Setup server request handlers

```java 
    
ServerRequestHandlers<Void> handlers = use(
        new ExceptionHandler<>(),
        new ResponseTimeHandler<>(),
        new TimeoutHandler<>(vertx),
        (f, n) -> (req, arg) -> {
            req.response().end("hello world!");
            n.accept(req, arg);
        });

var handler = handlers.apply(
        (e, a) -> logger.error(a),
        (e, a) -> logger.info(a));

server.requestHandler(req -> handler.accept(req, null))
        .listen();

```

## Implement server request handlers

```java 

public class HandlerImpl implements BiFunction<BiConsumer<HttpServerRequest, Throwable>,
        BiConsumer<HttpServerRequest, Void>, BiConsumer<HttpServerRequest, Void>> {

    @Override
    public BiConsumer<HttpServerRequest, Void> apply(
        BiConsumer<HttpServerRequest, Throwable> fail, BiConsumer<HttpServerRequest, Void> next) {
            return next;
    }
}

```

### Remarks

* Implementing handler still is quite verbose. 

## Example 

https://github.com/spriet2000/vertx-handlers-http-example


## Installation

```xml 

<dependency>
    <groupId>com.github.spriet2000</groupId>
    <artifactId>vertx-handlers-http</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>

```

