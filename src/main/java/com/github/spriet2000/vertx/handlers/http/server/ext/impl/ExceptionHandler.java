package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import io.vertx.core.http.HttpServerRequest;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ExceptionHandler implements BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<HttpServerRequest, Object>> {

    @Override
    public BiConsumer<HttpServerRequest, Object> apply(Consumer<Throwable> fail, Consumer<Object> next) {
        return (req, arg) -> {
            try {
                next.accept(arg);
            } catch (Exception e) {
                fail.accept(e);
            }
        };
    }
}
