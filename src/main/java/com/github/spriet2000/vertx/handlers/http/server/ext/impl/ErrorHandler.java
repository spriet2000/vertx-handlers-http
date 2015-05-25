package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import com.github.spriet2000.vertx.handlers.http.server.ServerController;
import com.github.spriet2000.vertx.handlers.http.server.ServerHandler;
import io.vertx.core.Handler;

public class ErrorHandler implements ServerController {

    public static ErrorHandler error() {
        return new ErrorHandler();
    }

    @Override
    public ServerHandler handle(Handler fail, Handler next) {
        return (req, res, args) -> {
            try {
                next.handle(args);
            } catch (Exception e) {
                fail.handle(e);
            }
        };
    }
}
