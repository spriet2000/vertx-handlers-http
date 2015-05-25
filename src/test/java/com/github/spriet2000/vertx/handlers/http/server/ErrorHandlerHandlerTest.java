package com.github.spriet2000.vertx.handlers.http.server;

import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ErrorHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ErrorResponse;
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
public class ErrorHandlerHandlerTest extends HttpTestBase {

    Logger logger = LoggerFactory.getLogger(ErrorHandlerHandlerTest.class);

    @Before
    public void setup() {
        server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
        client = vertx.createHttpClient(new HttpClientOptions());
    }

    @Test
    public void testErrorHandler() {
        ServerHandlers handlers = handlers(new ErrorHandler())
                .then((fail, next) -> (req, res, ags) -> {
                    String trouble = null;
                    logger.info(trouble.toString());
                }).exceptionHandler(new ErrorResponse());

        server.requestHandler(handlers)
                .listen(onSuccess(s -> client.request(HttpMethod.GET,
                        8080, "localhost", "/test", resp -> {
                            assertEquals(500, resp.statusCode());
                            resp.bodyHandler(body -> {
                                logger.info(body);
                                testComplete();
                            });
                        }).end()));
        await();
    }
}



