package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import com.github.spriet2000.vertx.handlers.http.server.RequestContext;
import io.vertx.core.Handler;

import java.util.function.BiFunction;

public class EndHandler implements BiFunction<Handler<Throwable>, Handler<Object>, Handler<RequestContext>> {

    @Override
    public Handler<RequestContext> apply(Handler<Throwable> fail, Handler<Object> next) {
        return context -> {
            if(context != null && ! context.request().isEnded()){
                context.request().response().end();
            }
            next.handle(context);
        };
    }
}
