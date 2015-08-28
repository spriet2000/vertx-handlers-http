package com.github.spriet2000.vertx.handlers.http.server;

import io.vertx.core.Handler;

import java.util.function.BiFunction;

public interface RequestHandler<T> extends BiFunction<Handler<Throwable>, Handler<Object>,Handler<T>> {
}
