package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import com.github.spriet2000.vertx.handlers.http.server.RequestContext;
import io.vertx.core.Handler;

public class EndResponseHandler implements java.util.function.BiFunction<Handler<Throwable>, Handler<Object>, Handler<RequestContext>> {

    @Override
    public Handler<RequestContext> apply(Handler<Throwable> fail, Handler<Object> next) {
        return context -> {
            if(context.request() != null && !context.request().isEnded()){
                context.request().response().end();
            }
            next.handle(context);
        };
    }
}
