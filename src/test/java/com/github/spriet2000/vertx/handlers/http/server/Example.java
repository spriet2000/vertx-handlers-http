package com.github.spriet2000.vertx.handlers.http.server;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;
import io.vertx.test.core.HttpTestBase;
import org.junit.Before;
import org.junit.Test;

import static com.github.spriet2000.vertx.handlers.http.server.ServerHandlers.handlers;
import static com.github.spriet2000.vertx.handlers.http.server.ext.impl.ErrorHandler.error;
import static com.github.spriet2000.vertx.handlers.http.server.ext.impl.ErrorResponse.errorResponse;
import static com.github.spriet2000.vertx.handlers.http.server.ext.impl.ResponseTimeHandler.responseTime;

@SuppressWarnings("unchecked")
public class Example extends HttpTestBase {

    Logger log = LoggerFactory.getLogger(Example.class);

    @Before
    public void setup() {
        server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
        client = vertx.createHttpClient(new HttpClientOptions());
    }

    @Test
    public void testHandler() {

        ServerHandlers commonHandlers = handlers(error(), responseTime());

        ServerHandlers errorHandler = handlers(commonHandlers)
                .then(errorResponse());

        ServerHandlers fooHandlers = handlers(commonHandlers)
                .then(new Foo1Handler(), new Foo2Handler()).with(FooContext::new)
                .exceptionHandler(errorHandler)
                .completeHandler((req, res, args) -> {
                    log.info("Handled request..");
                });

        server.requestHandler(fooHandlers).listen(
                onSuccess(s -> client.request(
                        HttpMethod.GET, 8080, "localhost", "/test", resp -> {
                            assertEquals(200, resp.statusCode());
                            assertEquals("1", resp.headers().get("result1"));
                            assertEquals("2", resp.headers().get("result2"));
                            log.info(resp.headers().get("X-Response-Time"));
                            testComplete();
                        }).end()));

        await();
    }

    interface Foo {
        String getResult1();

        void setResult1(String result);

        String getResult2();

        void setResult2(String result);
    }

    class Foo1Handler implements ServerController {

        @Override
        public ServerHandler<Foo> handle(Handler fail, Handler next) {
            return (req, res, args) -> {
                args.setResult1("1");
                args.setResult2("2");
                next.handle(args);
            };
        }
    }

    class Foo2Handler implements ServerController {

        @Override
        public ServerHandler<Foo> handle(Handler fail, Handler next) {
            return (req, res, args) -> {
                res.headers().add("result1", args.getResult1());
                res.headers().add("result2", args.getResult2());
                res.end();
                next.handle(args);
            };
        }
    }

    class FooContext implements Foo {
        private String result1;
        private String result2;

        @Override
        public String getResult1() {
            return result1;
        }

        @Override
        public void setResult1(String result) {
            result1 = result;
        }

        @Override
        public String getResult2() {
            return result2;
        }

        @Override
        public void setResult2(String result) {
            result2 = result;
        }
    }
}



