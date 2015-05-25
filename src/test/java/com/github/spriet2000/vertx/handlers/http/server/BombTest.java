package com.github.spriet2000.vertx.handlers.http.server;

import com.github.spriet2000.vertx.handlers.http.server.ext.impl.EndResponseHandler;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;
import io.vertx.test.core.HttpTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static com.github.spriet2000.vertx.handlers.http.server.ServerHandlers.handlers;

public class BombTest extends HttpTestBase {

    Logger logger = LoggerFactory.getLogger(BombTest.class);

    @Before
    public void setup() {
        server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
        client = vertx.createHttpClient(new HttpClientOptions());
    }

    @Test
    public void bomb() {
        ServerHandlers handlers = handlers(
                (fail, next) -> (req, res, args) -> next.handle(0),
                (fail, next) -> (req, res, args) -> {
                    assertEquals(0, args);
                    next.handle(1);
                },
                (fail, next) -> (req, res, args) -> {
                    assertEquals(1, args);
                    next.handle(0);
                },
                new EndResponseHandler());

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
