package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class TimeOutHandler<A> implements BiFunction<Consumer<Throwable>, Consumer<A>,
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
    public BiConsumer<HttpServerRequest, A> apply(Consumer<Throwable> fail, Consumer<A> next) {
        return (req, arg) -> {
            long id = vertx.setTimer(time, c -> fail.accept(new RuntimeException()));
            req.response().bodyEndHandler(e -> vertx.cancelTimer(id));
            next.accept(arg);
        };
    }
}
