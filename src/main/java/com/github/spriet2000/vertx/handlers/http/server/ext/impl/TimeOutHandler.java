package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import com.github.spriet2000.vertx.handlers.http.server.RequestContext;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.function.BiFunction;

public class TimeOutHandler implements BiFunction<Handler<Throwable>, Handler<Object>, Handler<RequestContext>> {

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
    public Handler<RequestContext> apply(Handler<Throwable> fail, Handler<Object> next) {
        return context -> {
            long id = vertx.setTimer(time, c -> fail.handle(new RuntimeException()));
            context.request().response().bodyEndHandler(e -> vertx.cancelTimer(id));
            next.handle(context);
        };
    }
}
