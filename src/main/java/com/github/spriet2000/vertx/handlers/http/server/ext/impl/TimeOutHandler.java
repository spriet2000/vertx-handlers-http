package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import com.github.spriet2000.vertx.handlers.http.server.ServerController;
import com.github.spriet2000.vertx.handlers.http.server.ServerHandler;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

public class TimeOutHandler implements ServerController {

    private final Vertx vertx;
    private final long time;

    public TimeOutHandler(Vertx vertx) {
        this.vertx = vertx;
        this.time = 1000;
    }

    public TimeOutHandler(Vertx vertx, long time) {
        this.vertx = vertx;
        this.time = time;
    }

    @Override
    public ServerHandler<Object> handle(Handler fail, Handler next) {
        return (req, res, args) -> {
            long id = vertx.setTimer(time, c -> {
                fail.handle(HttpResponseStatus.REQUEST_TIMEOUT.code());
            });
            res.bodyEndHandler(e -> {
                vertx.cancelTimer(id);
            });
            next.handle(args);
        };
    }
}
