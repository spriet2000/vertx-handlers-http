package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import com.github.spriet2000.vertx.handlers.http.server.RequestContext;
import io.vertx.core.Handler;

public class ResponseTimeHandler implements java.util.function.BiFunction<Handler<Throwable>, Handler<Object>, Handler<RequestContext>> {

    public static ResponseTimeHandler responseTime() {
        return new ResponseTimeHandler();
    }

    @Override
    public Handler<RequestContext> apply(Handler<Throwable> fail, Handler<Object> next) {
        return context -> {
            long start = System.nanoTime();
            context.request().response().headersEndHandler(event ->
                    context.request().response().headers().add("X-Response-Time",
                            String.format("%sms",
                                    (System.nanoTime() - start) / (double) 1000000)));
            next.handle(context);
        };
    }
}
