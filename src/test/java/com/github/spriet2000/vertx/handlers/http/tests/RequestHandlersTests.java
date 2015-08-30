package com.github.spriet2000.vertx.handlers.http.tests;

import com.github.spriet2000.handlers.Handlers;
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
        BiConsumer<StringBuilder, Object> success = (e, a) -> hitComplete.set(true);

        Handlers<StringBuilder> handlers = new Handlers<>(
                (f, n) -> (e, a) -> n.accept(a),
                (f, n) -> (e, a) -> n.accept(a),
                (f, n) -> (e, a) -> n.accept(a),
                (f, n) -> (e, a) -> n.accept(a));

        handlers.accept(null, null, exception, success);

        assertEquals(false, hitException.get());
        assertEquals(true, hitComplete.get());
    }

    @Test
    public void fail() {

        AtomicBoolean hitException = new AtomicBoolean(false);
        AtomicBoolean hitComplete = new AtomicBoolean(false);

        BiConsumer<Object, Throwable> exception = (e, a) -> hitException.set(true);
        BiConsumer<Void, Object> success = (e, a) -> hitComplete.set(true);

        Handlers<Void> handlers = new Handlers<>();

        handlers.andThen((f, n) -> (e, a) -> n.accept(a),
                (f, n) -> (e, a) -> n.accept(a),
                (f, n) -> (e, a) -> f.accept(new RuntimeException()),
                (f, n) -> (e, a) -> n.accept(a));

        handlers.accept(null, null, exception, success);

        assertEquals(true, hitException.get());
        assertEquals(false, hitComplete.get());
    }

    @Test
    public void testHttpContext() {

        BiConsumer<Object, Throwable> exception = (e, a) -> logger.error(a);
        BiConsumer<HttpServerRequest, Object> success = (e, a) -> logger.info(a);

        Handlers<HttpServerRequest> handlers = new Handlers<>();
        handlers.andThen((f, n) -> (e, a) -> n.accept(a),
                (f, n) -> (e, a) -> e.response().end());

        vertx.createHttpServer(new HttpServerOptions().setPort(8080))
                .requestHandler(e -> handlers.accept(e, null, exception, success))
        .listen(onSuccess(s ->
                vertx.createHttpClient(new HttpClientOptions())
                        .getNow(8080, "localhost", "/test", res -> testComplete())));

        await();
    }
}
