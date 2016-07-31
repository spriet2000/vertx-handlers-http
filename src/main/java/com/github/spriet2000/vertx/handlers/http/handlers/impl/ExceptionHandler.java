package com.github.spriet2000.vertx.handlers.http.handlers.impl;

import io.vertx.core.http.HttpServerRequest;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ExceptionHandler<A> implements BiFunction<BiConsumer<HttpServerRequest, Throwable>, BiConsumer<HttpServerRequest, A>,
        BiConsumer<HttpServerRequest, A>> {

    @Override
    public BiConsumer<HttpServerRequest, A> apply(BiConsumer<HttpServerRequest, Throwable> fail,
                                                  BiConsumer<HttpServerRequest, A> next) {
        return (req, arg) -> {
            try {
                next.accept(req, arg);
            } catch (Exception e) {
                fail.accept(req, e);
            }
        };
    }
}
