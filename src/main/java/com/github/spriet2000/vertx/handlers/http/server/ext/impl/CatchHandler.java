package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import com.github.spriet2000.vertx.handlers.http.server.RequestContext;
import com.github.spriet2000.vertx.handlers.http.server.RequestHandler;
import io.vertx.core.Handler;

public class CatchHandler implements RequestHandler<RequestContext> {

    public static CatchHandler error() {
        return new CatchHandler();
    }

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
