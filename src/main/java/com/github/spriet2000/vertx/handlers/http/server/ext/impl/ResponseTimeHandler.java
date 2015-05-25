package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import com.github.spriet2000.vertx.handlers.http.server.ServerController;
import com.github.spriet2000.vertx.handlers.http.server.ServerHandler;
import io.vertx.core.Handler;

public class ResponseTimeHandler implements ServerController {

    public static ResponseTimeHandler responseTime() {
        return new ResponseTimeHandler();
    }

    @Override
    public ServerHandler<Object> handle(Handler fail, Handler next) {
        return (req, res, args) -> {
            long start = System.nanoTime();
            res.headersEndHandler(event ->
                    res.headers().add("X-Response-Time",
                            String.format("%sms",
                                    (System.nanoTime() - start) / (double) 1000000)));
            next.handle(args);
        };
    }
}
