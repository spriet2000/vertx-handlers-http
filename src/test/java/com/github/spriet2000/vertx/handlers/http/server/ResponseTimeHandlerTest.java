package com.github.spriet2000.vertx.handlers.http.server;

import com.github.spriet2000.vertx.handlers.http.server.ext.impl.EndResponseHandler;
import com.github.spriet2000.vertx.handlers.http.server.ext.impl.ResponseTimeHandler;
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
public class ResponseTimeHandlerTest extends HttpTestBase {

    Logger logger = LoggerFactory.getLogger(ResponseTimeHandlerTest.class);

    @Before
    public void setup() {
        server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
        client = vertx.createHttpClient(new HttpClientOptions());
    }

    @Test
    public void testHandler() {
        ServerHandlers handlers = handlers(new ResponseTimeHandler(),
                new EndResponseHandler());

        server.requestHandler(handlers)
                .listen(onSuccess(s -> client.request(HttpMethod.GET,
                        8080, "localhost", "/test", resp -> {
                            assertEquals(200, resp.statusCode());
                            assertNotNull(resp.headers().get("X-Response-Time"));
                            logger.info(resp.headers().get("X-Response-Time"));
                            testComplete();
                        }).end()));
        await();
    }
}



