package com.github.spriet2000.vertx.handlers.http.tests;

import com.github.spriet2000.vertx.handlers.http.server.RequestHandlers;
import io.vertx.core.Handler;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;

public class RequestHandlersTests {

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
    public void test(){


    }

}
