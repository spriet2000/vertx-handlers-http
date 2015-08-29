package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;

import java.util.function.BiFunction;

public class ResponseTimeHandler implements BiFunction<Handler<Throwable>, Handler<Object>, Handler<HttpServerRequest>> {

    public static ResponseTimeHandler responseTime() {
        return new ResponseTimeHandler();
    }

    @Override
    public Handler<HttpServerRequest> apply(Handler<Throwable> fail, Handler<Object> next) {
        return context -> {
            long start = System.nanoTime();
            context.response().headersEndHandler(event ->
                    context.response().headers().add("X-Response-Time",
                            String.format("%sms",
                                    (System.nanoTime() - start) / (double) 1000000)));
            next.handle(context);
        };
    }
}
