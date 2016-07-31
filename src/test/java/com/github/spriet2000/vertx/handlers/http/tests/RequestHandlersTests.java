package com.github.spriet2000.vertx.handlers.http.tests;

import com.github.spriet2000.vertx.handlers.http.impl.ServerRequestHandlers;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.test.core.HttpTestBase;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

import static com.github.spriet2000.vertx.handlers.http.impl.ServerRequestHandlers.use;


@SuppressWarnings("ALL")
public class RequestHandlersTests extends HttpTestBase {

    private Logger logger = LoggerFactory.getLogger(RequestHandlersTests.class);

    @Test
    public void success() {

        AtomicBoolean hitException = new AtomicBoolean(false);
        AtomicBoolean hitComplete = new AtomicBoolean(false);

        BiConsumer<HttpServerRequest, Throwable> exception = (e, a) -> hitException.set(true);
        BiConsumer<HttpServerRequest, Void> success = (e, a) -> hitComplete.set(true);

        ServerRequestHandlers<Void> handlers = use(
            (f, n) -> n,
            (f, n) -> n,
            (f, n) -> n,
            (f, n) -> n);

        handlers.apply(exception, success).accept(null, null);

        assertEquals(false, hitException.get());
        assertEquals(true, hitComplete.get());
    }

    @Test
    public void fail() {

        AtomicBoolean hitException = new AtomicBoolean(false);
        AtomicBoolean hitComplete = new AtomicBoolean(false);

        BiConsumer<HttpServerRequest, Throwable> exception = (e, a) -> hitException.set(true);
        BiConsumer<HttpServerRequest, Void> success = (e, a) -> hitComplete.set(true);

        ServerRequestHandlers<Void> handlers = use(
            (f, n) -> n,
            (f, n) -> n,
            (f, n) -> (e, a) -> f.accept(e, new RuntimeException()),
            (f, n) -> n);

        handlers.apply(exception, success).accept(null, null);

        assertEquals(true, hitException.get());
        assertEquals(false, hitComplete.get());
    }

}
