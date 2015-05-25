# Vert.x handlers-http

Handlers-http provides a minimal and adaptable interface for developing web applications on the vert-x3 platform.

[![Build Status](https://travis-ci.org/spriet2000/vertx-handlers-http.svg?branch=master)](https://travis-ci.org/spriet2000/vertx-handlers-http)

## Example

```java 

ServerHandlers commonHandlers = handlers(error(), responseTime());

ServerHandlers errorHandler = handlers(commonHandlers)
        .then(errorResponse());

ServerHandlers fooHandlers = handlers(commonHandlers)
        .then(new Foo1Handler(), new Foo2Handler()).with(FooContext::new)
        .exceptionHandler(errorHandler)
        .completeHandler((req, res, args) -> {
            log.info("Handled request..");
        });

server.requestHandler(fooHandlers).listen(
        onSuccess(s -> client.request(
                HttpMethod.GET, 8080, "localhost", "/test", resp -> {
                    assertEquals(200, resp.statusCode());
                    assertEquals("1", resp.headers().get("result1"));
                    assertEquals("2", resp.headers().get("result2"));
                    log.info(resp.headers().get("X-Response-Time"));
                    testComplete();
                }).end()));

```

### Test handlers

```java

class Foo1Handler implements ServerController {

    @Override
    public ServerHandler<Foo> handle(Handler fail, Handler next) {
        return (req, res, args) -> {
            args.setResult1("1");
            args.setResult2("2");
            next.handle(args);
        };
    }
}

```

```java

class Foo2Handler implements ServerController {

    @Override
    public ServerHandler<Foo> handle(Handler fail, Handler next) {
        return (req, res, args) -> {
            res.headers().add("result1", args.getResult1());
            res.headers().add("result2", args.getResult2());
            res.end();
            next.handle(args);
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
