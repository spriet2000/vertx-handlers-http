package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import io.vertx.core.http.HttpServerRequest;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class EndHandler<A> implements BiFunction<Consumer<Throwable>, Consumer<A>,
        BiConsumer<HttpServerRequest, A>> {

    @Override
    public BiConsumer<HttpServerRequest, A> apply(Consumer<Throwable> fail, Consumer<A> next) {
        return (req, arg) -> {
            if (req != null && !req.isEnded()) {
                req.response().end();
            }
            next.accept(arg);
        };
    }
}
