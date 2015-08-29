package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;

import java.util.function.BiFunction;

public class ResponseTimeHandler implements BiFunction<Handler<Throwable>, Handler<Object>, Handler<HttpServerRequest>> {

    @Override
    public Handler<HttpServerRequest> apply(Handler<Throwable> fail, Handler<Object> next) {
        return request -> {
            long start = System.nanoTime();
            request.response().headersEndHandler(e -> {
                    if (!request.response().headWritten()) {
                        request.response().headers().add("X-Response-Time",
                                String.format("%sms", (System.nanoTime() - start) / (double) 1000000));
                        e.complete();
                    }
                });
            next.handle(request);
        };
    }
}
