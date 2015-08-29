package com.github.spriet2000.vertx.handlers.http.server;

import io.vertx.core.http.HttpServerRequest;

public interface RequestContext {
    HttpServerRequest request();
}
