package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;

import java.util.function.BiFunction;

public class EndResponseHandler implements BiFunction<Handler<Throwable>, Handler<Object>, Handler<HttpServerRequest>> {

    @Override
    public Handler<HttpServerRequest> apply(Handler<Throwable> fail, Handler<Object> next) {
        return context -> {
            if(context != null && !context.isEnded()){
                context.response().end();
            }
            next.handle(context);
        };
    }
}
