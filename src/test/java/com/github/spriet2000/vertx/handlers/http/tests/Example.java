package com.github.spriet2000.vertx.handlers.http.tests;

import com.github.spriet2000.handlers.Handlers;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.EndHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ExceptionHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ResponseTimeHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.TimeOutHandler;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.test.core.HttpTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.function.BiConsumer;


public class Example extends HttpTestBase {

    Logger logger = LoggerFactory.getLogger(Example.class);

    @Before
    public void setup() {
        server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
        client = vertx.createHttpClient(new HttpClientOptions());
    }

    @Test
    public void example1() {

        BiConsumer<Object, Throwable> exception = (e, a) -> logger.error(a);
        BiConsumer<HttpServerRequest, Void> success = (e, a) -> logger.info(a);

        Handlers<HttpServerRequest, Void> handlers = new Handlers<>();

        handlers.andThen(new ExceptionHandler(),
                new ResponseTimeHandler(),
                new TimeOutHandler(vertx),
                new EndHandler());

        server.requestHandler(e -> handlers.accept(e, null, exception, success))
                .listen();
    }
}
