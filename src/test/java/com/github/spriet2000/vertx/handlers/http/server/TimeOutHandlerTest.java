package com.github.spriet2000.vertx.handlers.http.server;

import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ErrorResponse;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.TimeOutHandler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;
import io.vertx.test.core.HttpTestBase;
import org.junit.Before;
import org.junit.Test;

import static com.github.spriet2000.vertx.handlers.http.server.ServerHandlers.handlers;

@SuppressWarnings("unchecked")
public class TimeOutHandlerTest extends HttpTestBase {

    Logger logger = LoggerFactory.getLogger(TimeOutHandlerTest.class);

    @Before
    public void setup() {
        server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
        client = vertx.createHttpClient(new HttpClientOptions());
    }

    @Test
    public void testTimeOutHandler() {
        ServerHandlers handlers = handlers(
                new TimeOutHandler(vertx, 900),
                (fail, next) -> (req, res, args) ->
                        Vertx.vertx().setTimer(900, c -> next.handle(args)),
                (fail, next) -> (req, res, args) -> logger.info("handled"));
        handlers.exceptionHandler(new ErrorResponse());

        server.requestHandler(handlers)
                .listen(onSuccess(s -> client.request(HttpMethod.GET,
                        8080, "localhost", "/test", resp -> {
                            assertEquals(408, resp.statusCode());
                            resp.bodyHandler(body -> {
                                logger.info(body);
                                testComplete();
                            });
                        }).end()));
        await();
    }
}



