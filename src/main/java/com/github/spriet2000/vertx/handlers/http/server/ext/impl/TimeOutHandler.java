package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class TimeOutHandler<A> implements BiFunction<BiConsumer<HttpServerRequest, Throwable>, BiConsumer<HttpServerRequest, A>,
        BiConsumer<HttpServerRequest, A>> {

    private final Vertx vertx;
    private final long time;

    public TimeOutHandler(Vertx vertx) {
        this.vertx = vertx;
        this.time = 1000;
    }

    public TimeOutHandler(Vertx vertx, long time) {
        this.vertx = vertx;
        this.time = time;
    }

    @Override
    public BiConsumer<HttpServerRequest, A> apply(BiConsumer<HttpServerRequest, Throwable> fail,
                                                  BiConsumer<HttpServerRequest, A> next) {
        return (req, arg) -> {
            long id = vertx.setTimer(time, c -> fail.accept(req, new RuntimeException()));
            req.response().bodyEndHandler(e -> vertx.cancelTimer(id));
            next.accept(req, arg);
        };
    }
}
