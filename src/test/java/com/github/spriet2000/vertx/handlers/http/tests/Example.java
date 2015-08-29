package com.github.spriet2000.vertx.handlers.http.tests;

import com.github.spriet2000.vertx.handlers.http.server.RequestHandlers;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.test.core.HttpTestBase;
import org.junit.Before;
import org.junit.Test;


public class Example  extends HttpTestBase {

    @Before
    public void setup() {
        server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
        client = vertx.createHttpClient(new HttpClientOptions());
    }

    @Test
    public void bomb(){

        Handler<Throwable> exception = e -> {};
        Handler<Object> success = e -> {};

        RequestHandlers<HttpServerRequest> handlers =
                new RequestHandlers<>(exception, success);

        handlers.then((f, n) -> n::handle,
                (f, n) -> n::handle,
                (f, n) -> n::handle,
                (f, n) -> n::handle,
                (f, n) -> n::handle,
                (f, n) -> request -> request.response().end());

        server.requestHandler(handlers::handle)
                .listen();
    }
}
