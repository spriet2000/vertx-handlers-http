package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import com.github.spriet2000.vertx.handlers.http.server.RequestContext;
import io.vertx.core.Handler;

public class CatchHandler implements java.util.function.BiFunction<Handler<Throwable>, Handler<Object>, Handler<RequestContext>> {

    @Override
    public Handler<RequestContext> apply(Handler<Throwable> fail, Handler<Object> next) {
        return context -> {
            try {
                next.handle(context);
            } catch (Exception e) {
                fail.handle(e);
            }
        };
    }
}
