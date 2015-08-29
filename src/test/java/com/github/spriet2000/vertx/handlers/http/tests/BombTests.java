package com.github.spriet2000.vertx.handlers.http.tests;


import com.github.spriet2000.vertx.handlers.http.server.RequestContext;
import com.github.spriet2000.vertx.handlers.http.server.RequestHandlers;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.EndHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ExceptionHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ResponseTimeHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.TimeOutHandler;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerOptions;
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

        Handler<Throwable> exception = logger::error;
        Handler<Object> success = logger::info;

        RequestHandlers handlers = new RequestHandlers(exception, success);

        handlers.then(new ExceptionHandler(),
                new ResponseTimeHandler(),
                new TimeOutHandler(vertx),
                new EndHandler());

        int bombs = 2000;
        CountDownLatch startSignal = new CountDownLatch(bombs);
        server.requestHandler(req -> handlers.handle(req, RequestContext::new)).listen(onSuccess(s -> {
            client = vertx.createHttpClient(new HttpClientOptions());
            for (int i = 0; i < bombs; i++) {
                client.getNow(8080, "localhost", "/test",
                        res -> {
                            logger.info(startSignal.getCount());
                            logger.info(res.headers().get("X-Response-Time"));
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
