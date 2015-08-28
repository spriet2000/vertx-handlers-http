package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import com.github.spriet2000.vertx.handlers.http.server.RequestContext;
import com.github.spriet2000.vertx.handlers.http.server.RequestHandler;
import io.vertx.core.Handler;

public class EndResponseHandler implements RequestHandler<RequestContext> {

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
