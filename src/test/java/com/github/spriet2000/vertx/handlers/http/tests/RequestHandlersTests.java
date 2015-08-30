package com.github.spriet2000.vertx.handlers.http.tests;

import com.github.spriet2000.vertx.handlers.http.server.RequestHandlers;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.test.core.HttpTestBase;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class RequestHandlersTests extends HttpTestBase {

    Logger logger = LoggerFactory.getLogger(RequestHandlersTests.class);

    @Test
    public void success() {

        AtomicBoolean hitException = new AtomicBoolean(false);
        AtomicBoolean hitComplete = new AtomicBoolean(false);

        BiConsumer<Object, Throwable> exception = (e, a) -> hitException.set(true);
        BiConsumer<Object, Object> success = (e, a) -> hitComplete.set(true);

        RequestHandlers<Void, Void> handlers = new RequestHandlers<>(exception, success);

        handlers.andThen((f, n) -> (e, a) -> n.accept(a),
                (f, n) -> (e, a) -> n.accept(a),
                (f, n) -> (e, a) -> n.accept(a),
                (f, n) -> (e, a) -> n.accept(a));

        handlers.handle(null, null);

        assertEquals(false, hitException.get());
        assertEquals(true, hitComplete.get());
    }

    @Test
    public void fail() {

        AtomicBoolean hitException = new AtomicBoolean(false);
        AtomicBoolean hitComplete = new AtomicBoolean(false);

        BiConsumer<Object, Throwable> exception = (e, a) -> hitException.set(true);
        BiConsumer<Object, Object> success = (e, a) -> hitComplete.set(true);

        RequestHandlers<Void, Void> handlers = new RequestHandlers<>(exception, success);

        handlers.andThen((f, n) -> (e, a) -> n.accept(a),
                (f, n) -> (e, a) -> n.accept(a),
                (f, n) -> (e, a) -> f.accept(new RuntimeException()),
                (f, n) -> (e, a) -> n.accept(a));

        handlers.handle(null, null);

        assertEquals(true, hitException.get());
        assertEquals(false, hitComplete.get());
    }

    @Test
    public void testHttpContext() {

        BiConsumer<Object, Throwable> exception = (e, a) -> logger.error(a);
        BiConsumer<Object, Object> success = (e, a) -> logger.info(a);

        RequestHandlers<HttpServerRequest, Object> handlers = new RequestHandlers<>(exception, success);
        handlers.andThen((f, n) -> (e, a) -> n.accept(a),
                (f, n) -> (e, a) -> e.response().end());

        vertx.createHttpServer(new HttpServerOptions().setPort(8080))
                .requestHandler(e -> handlers.handle(e, null))
                .listen(onSuccess(s ->
                        vertx.createHttpClient(new HttpClientOptions())
                                .getNow(8080, "localhost", "/test", res -> testComplete())));

        await();
    }
}
