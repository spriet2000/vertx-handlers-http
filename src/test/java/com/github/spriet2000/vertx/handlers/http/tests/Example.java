package com.github.spriet2000.vertx.handlers.http.tests;

import com.github.spriet2000.vertx.handlers.core.http.ServerRequestHandlers;
import com.github.spriet2000.vertx.handlers.extensions.basic.impl.ExceptionHandler;
import com.github.spriet2000.vertx.handlers.extensions.basic.impl.ResponseTimeHandler;
import com.github.spriet2000.vertx.handlers.extensions.timeout.impl.TimeoutHandler;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.test.core.HttpTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.function.BiConsumer;

import static com.github.spriet2000.vertx.handlers.core.http.ServerRequestHandlers.build;


public class Example extends HttpTestBase {

    private Logger logger = LoggerFactory.getLogger(Example.class);

    @Before
    public void setup() {
        server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
        client = vertx.createHttpClient(new HttpClientOptions());
    }

    @Test
    public void example1() {

        ServerRequestHandlers<Void> chain = build(
                new ExceptionHandler<>(),
                new ResponseTimeHandler<>(),
                new TimeoutHandler<>(vertx),
                (f, n) -> (req, arg) -> {
                    req.response().end("hello world!");
                    n.accept(req, arg);
                });

        BiConsumer<HttpServerRequest, Void> handler = chain.apply(
                (e, a) -> logger.error(a),
                (e, a) -> logger.info(a));

        server.requestHandler(req -> handler.accept(req, null))
                .listen();

    }
}
