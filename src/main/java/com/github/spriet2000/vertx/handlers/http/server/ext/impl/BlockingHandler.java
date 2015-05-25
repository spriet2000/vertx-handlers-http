package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import com.github.spriet2000.vertx.handlers.http.server.ServerController;
import com.github.spriet2000.vertx.handlers.http.server.ServerHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

public class BlockingHandler<T> implements ServerController {

    private final Vertx vertx;
    private final Handler<Future<T>> handler;

    public BlockingHandler(Vertx vertx, Handler<Future<T>> handler) {

        this.vertx = vertx;
        this.handler = handler;
    }

    @Override
    public ServerHandler handle(Handler fail, Handler next) {
        Handler<AsyncResult<T>> resultHandler = result -> {
            if (result.succeeded()) {
                next.handle(result.result());
            } else {
                fail.handle(result.cause());
            }
        };
        return (req, res, arg) ->
                vertx.executeBlocking(handler, resultHandler);
    }
}
