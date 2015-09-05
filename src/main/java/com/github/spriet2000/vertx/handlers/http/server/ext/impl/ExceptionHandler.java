package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import io.vertx.core.http.HttpServerRequest;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ExceptionHandler<A> implements BiFunction<Consumer<Throwable>, Consumer<Object>,
        BiConsumer<HttpServerRequest, A>> {

    @Override
    public BiConsumer<HttpServerRequest, A> apply(Consumer<Throwable> fail, Consumer<Object> next) {
        return (req, arg) -> {
            try {
                next.accept(arg);
            } catch (Exception e) {
                fail.accept(e);
            }
        };
    }
}
