package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import com.github.spriet2000.vertx.handlers.http.server.ServerController;
import com.github.spriet2000.vertx.handlers.http.server.ServerHandler;
import io.vertx.core.Handler;

public class EndResponseHandler implements ServerController {

    @Override
    public ServerHandler<Object> handle(Handler fail, Handler next) {
        return (req, res, args) -> {
            if (res != null
                    && !res.ended()) {
                res.end();
            }
            next.handle(args);
        };
    }
}
