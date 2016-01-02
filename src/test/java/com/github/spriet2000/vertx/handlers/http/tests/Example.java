package com.github.spriet2000.vertx.handlers.http.tests;

import com.github.spriet2000.handlers.BiHandlers;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ExceptionHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ResponseTimeHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.timeout.impl.TimeoutHandler;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.test.core.HttpTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static com.github.spriet2000.handlers.BiHandlers.compose;


public class Example extends HttpTestBase {

    Logger logger = LoggerFactory.getLogger(Example.class);

    @Before
    public void setup() {
        server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
        client = vertx.createHttpClient(new HttpClientOptions());
    }

    @Test
    public void example1() {

        BiHandlers<HttpServerRequest, Void> handlers = compose(
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

    }
}
