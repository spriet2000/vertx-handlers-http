package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import com.github.spriet2000.vertx.handlers.http.server.Request;
import io.vertx.core.Handler;

import java.util.function.BiFunction;

public class ExceptionHandler implements BiFunction<Handler<Throwable>, Handler<Object>, Handler<Request>> {

    @Override
    public Handler<Request> apply(Handler<Throwable> fail, Handler<Object> next) {
        return context -> {
            try {
                next.handle(context);
            } catch (Exception e) {
                fail.handle(e);
            }
        };
    }
}
