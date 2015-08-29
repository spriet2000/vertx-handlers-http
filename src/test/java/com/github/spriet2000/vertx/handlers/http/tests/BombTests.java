package com.github.spriet2000.vertx.handlers.http.tests;


import com.github.spriet2000.vertx.handlers.http.server.RequestHandlers;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.test.core.HttpTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class BombTests extends HttpTestBase {

    Logger logger = LoggerFactory.getLogger(BombTests.class);

    @Before
    public void setup() {
        server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
        client = vertx.createHttpClient(new HttpClientOptions());
    }

    @Test
    public void bomb(){

        Handler<Throwable> exception = e -> {};
        Handler<Object> success = e -> {};

        RequestHandlers<HttpServerRequest> handlers = new RequestHandlers<>(exception, success);

        handlers.then((f, n) -> n::handle,
                (f, n) -> n::handle,
                (f, n) -> n::handle,
                (f, n) -> n::handle,
                (f, n) -> n::handle,
                (f, n) -> n::handle,
                (f, n) -> n::handle,
                (f, n) -> n::handle,
                (f, n) -> n::handle,
                (f, n) -> request -> request.response().end());

        int bombs = 2000;
        CountDownLatch startSignal = new CountDownLatch(bombs);
        server.requestHandler(handlers)
                .listen(onSuccess(s -> {
                    client = vertx.createHttpClient(new HttpClientOptions());
                    for (int i = 0; i < bombs; i++) {
                        client.getNow(8080, "localhost", "/test",
                                res -> {
                                    logger.info(startSignal.getCount());
                                    assertEquals(200, res.statusCode());
                                    startSignal.countDown();
                                });
                    }
                }));

        try {
            startSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
