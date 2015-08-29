package com.github.spriet2000.vertx.handlers.http.tests;

import com.github.spriet2000.vertx.handlers.http.server.RequestHandlers;
import io.vertx.core.Handler;
import io.vertx.core.http.*;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.test.core.HttpTestBase;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class RequestHandlersTests extends HttpTestBase {

    Logger logger = LoggerFactory.getLogger(RequestHandlersTests.class);

    @Test
    public void success(){

        AtomicBoolean hitException = new AtomicBoolean(false);
        AtomicBoolean hitComplete = new AtomicBoolean(false);

        Handler<Throwable> exception = e -> hitException.set(true);
        Handler<Object> success = s -> hitComplete.set(true);

        RequestHandlers<Void> handlers = new RequestHandlers<>(exception, success);

        handlers.then((f, n) -> n::handle,
                (f, n) -> n::handle,
                (f, n) -> n::handle,
                (f, n) -> n::handle);

        handlers.handle(null);

        assertEquals(false, hitException.get());
        assertEquals(true, hitComplete.get());
    }

    @Test
    public void fail(){

        AtomicBoolean hitException = new AtomicBoolean(false);
        AtomicBoolean hitComplete = new AtomicBoolean(false);

        Handler<Throwable> exception = e -> hitException.set(true);
        Handler<Object> success = s -> hitComplete.set(true);

        RequestHandlers<Void> handlers = new RequestHandlers<>(exception, success);

        handlers.then((f, n) -> n::handle,
                (f, n) -> n::handle,
                (f, n) -> c -> f.handle(new RuntimeException()),
                (f, n) -> n::handle);

        handlers.handle(null);

        assertEquals(true, hitException.get());
        assertEquals(false, hitComplete.get());
    }

    @Test
    public void testHttpServerRequest(){

        Handler<Throwable> exception = logger::error;
        Handler<Object> success = logger::info;

        RequestHandlers<HttpServerRequest> handlers = new RequestHandlers<>(exception, success);
        handlers.then((f, n) -> n::handle,
                (f, n) -> req -> req.response().end());

        vertx.createHttpServer(new HttpServerOptions().setPort(8080))
                .requestHandler(handlers).listen(onSuccess(s ->
                    vertx.createHttpClient(new HttpClientOptions())
                        .getNow(8080, "localhost", "/test", res -> testComplete())));

        await();
    }

    @Test
    public void testHttpContext(){

        Handler<Throwable> exception = logger::error;
        Handler<Object> success = logger::info;

        RequestHandlers<HttpContext> handlers = new RequestHandlers<>(exception, success);
        handlers.then((f, n) -> n::handle,
                (f, n) -> ctx -> ctx.request().response().end());

        vertx.createHttpServer(new HttpServerOptions().setPort(8080))
                .requestHandler(e -> handlers.handle(e, req -> new HttpContext(e)))
                .listen(onSuccess(s ->
                        vertx.createHttpClient(new HttpClientOptions())
                                .getNow(8080, "localhost", "/test", res -> testComplete())));

        await();
    }

}
