package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import io.vertx.core.http.HttpServerRequest;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ResponseTimeHandler<A> implements BiFunction<BiConsumer<HttpServerRequest, Throwable>, BiConsumer<HttpServerRequest, A>,
        BiConsumer<HttpServerRequest, A>> {

    @Override
    public BiConsumer<HttpServerRequest, A> apply(BiConsumer<HttpServerRequest, Throwable> fail, BiConsumer<HttpServerRequest, A> next) {
        return (req, arg) -> {
            long start = System.nanoTime();
            req.response().headersEndHandler(e -> {
                if (!req.response().headWritten()) {
                    req.response().headers().add("X-Response-Time",
                            String.format("%sms", (System.nanoTime() - start) / (double) 1000000));
                }
            });
            next.accept(req, arg);
        };
    }
}
