# Vert.x handlers-http

Handlers-http provides a minimal and adaptable interface for developing web applications on the vert-x3 platform.

Handlers-http is an open webframework for the vert-x3 platform. The middleware layer doesn't depend on any custom classes, so components should be easily inter-changeable with different webframeworks.

Inspired by ['Build You Own Web Framework In Go'](https://www.nicolasmerouze.com/build-web-framework-golang)

[![Build Status](https://travis-ci.org/spriet2000/vertx-handlers-http.svg?branch=master)](https://travis-ci.org/spriet2000/vertx-handlers-http)

## Example

```java 
    
Handlers<HttpServerRequest, Object> handlers = compose(
        new ExceptionHandler<>(),
        new ResponseTimeHandler<>(),
        new TimeOutHandler<>(vertx),
        new EndHandler<>());

BiConsumer<HttpServerRequest, Object> handler = handlers.apply(
        (e, a) -> logger.error(a),
        (e, a) -> logger.info(a));

server.requestHandler(req -> handler.accept(req, null))
        .listen();

```

### Example handler

``` java

public class EndHandler<A> implements 
        BiFunction<Consumer<Throwable>, Consumer<Object>,
            BiConsumer<HttpServerRequest, A>> {

    @Override
    public BiConsumer<HttpServerRequest, A> 
        apply(Consumer<Throwable> fail, Consumer<A> next) {
            return (req, arg) -> {
                req.response().end("hello world!");
                next.accept(arg);
            };
    }
}    

```
## Example project

[https://github.com/spriet2000/vertx-handlers-http-example](https://github.com/spriet2000/vertx-handlers-http-example)

## Installation

### Maven

```xml

<dependency>
    <groupId>com.github.spriet2000</groupId>
    <artifactId>handlers</artifactId>
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
