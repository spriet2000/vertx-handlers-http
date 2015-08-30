package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import io.vertx.core.http.HttpServerRequest;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class EndHandler<T> implements BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<HttpServerRequest, T>> {

    @Override
    public BiConsumer<HttpServerRequest, T> apply(Consumer<Throwable> fail, Consumer<Object> next) {
        return (req, arg) -> {
            if (req != null && !req.isEnded()) {
                req.response().end();
            }
            next.accept(arg);
        };
    }
}
