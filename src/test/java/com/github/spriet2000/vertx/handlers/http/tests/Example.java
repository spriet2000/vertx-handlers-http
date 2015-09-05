package com.github.spriet2000.vertx.handlers.http.tests;

import com.github.spriet2000.handlers.Handlers;
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
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static com.github.spriet2000.handlers.Handlers.compose;


public class Example extends HttpTestBase {

    Logger logger = LoggerFactory.getLogger(Example.class);

    @Before
    public void setup() {
        server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
        client = vertx.createHttpClient(new HttpClientOptions());
    }

    @Test
    public void example1() {

        Handlers<HttpServerRequest, Void> handlers = compose(
                new ExceptionHandler(),
                new ResponseTimeHandler(),
                new TimeOutHandler(vertx),
                new EndHandler());

        server.requestHandler(req -> handlers.apply(
                (e, a) -> logger.error(a),
                (e, a) -> logger.info(a))
                .accept(req, null))
                .listen();
    }

    public class EndHandler<T> implements
            BiFunction<Consumer<Throwable>, Consumer<Object>,
                BiConsumer<HttpServerRequest, T>> {

        @Override
        public BiConsumer<HttpServerRequest, T>
            apply(Consumer<Throwable> fail, Consumer<Object> next) {
                return (req, arg) -> {
                    req.response().end("hello world!");
                    next.accept(arg);
                };
            }
    }
}
