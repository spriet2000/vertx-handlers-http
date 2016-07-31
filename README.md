# Vert.x handlers-http

Handlers-http provides a minimal and adaptable interface for developing web applications on the vert-x3 platform.

Handlers-http is an open webframework for the vert-x3 platform. The middleware layer doesn't depend on any custom classes, so components should be easily inter-changeable with different webframeworks.

Inspired by ['Build You Own Web Framework In Go'](https://www.nicolasmerouze.com/build-web-framework-golang)

[![Build Status](https://travis-ci.org/spriet2000/vertx-handlers-http.svg?branch=master)](https://travis-ci.org/spriet2000/vertx-handlers-http)

## Setup server request handlers

```java 
    
    ServerRequestHandlers<Void> handlers = build(
            new ExceptionHandler<>(),
            new ResponseTimeHandler<>(),
            new TimeoutHandler<>(vertx),
            (f, n) -> (req, arg) -> {
                req.response().end("hello world!");
                n.accept(req, arg);
            });

    BiConsumer<HttpServerRequest, Void> handler = handlers.apply(
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
        public BiConsumer<HttpServerRequest, Void> apply(BiConsumer<HttpServerRequest, Throwable> fail,
                                                     BiConsumer<HttpServerRequest, Void> next) {
            return next;
        }
    }

```


### Remarks

* When var, or something like, enters the JAVA language, defining handlers is quite less verbose. http://openjdk.java.net/jeps/286
* Implementing handler still is quite verbose. 