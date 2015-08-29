package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import com.github.spriet2000.vertx.handlers.http.server.Request;
import io.vertx.core.Handler;

import java.util.function.BiFunction;

public class ResponseTimeHandler implements BiFunction<Handler<Throwable>, Handler<Object>, Handler<Request>> {

    @Override
    public Handler<Request> apply(Handler<Throwable> fail, Handler<Object> next) {
        return context -> {
            long start = System.nanoTime();
            context.request().response().headersEndHandler(e -> {
                    if (!context.request().response().headWritten()) {
                        context.request().response().headers().add("X-Response-Time",
                                String.format("%sms", (System.nanoTime() - start) / (double) 1000000));
                        e.complete();
                    }
                });
            next.handle(context);
        };
    }
}
