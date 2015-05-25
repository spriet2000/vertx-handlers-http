package com.github.spriet2000.vertx.handlers.http.server;

import com.github.spriet2000.vertx.handlers.Handler3;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

public interface ServerHandler<Argument> extends Handler3<HttpServerRequest, HttpServerResponse, Argument> {

}
