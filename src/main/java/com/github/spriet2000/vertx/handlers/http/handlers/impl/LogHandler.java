package com.github.spriet2000.vertx.handlers.http.handlers.impl;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class LogHandler<A> implements BiFunction<BiConsumer<HttpServerRequest, Throwable>, BiConsumer<HttpServerRequest, A>,
        BiConsumer<HttpServerRequest, A>> {

    Logger logger = LoggerFactory.getLogger(LogHandler.class);

    @Override
    public BiConsumer<HttpServerRequest, A> apply(BiConsumer<HttpServerRequest, Throwable> fail,
                                                  BiConsumer<HttpServerRequest, A> next) {
        return (req, arg) -> {
            StringBuilder builder1 = new StringBuilder();
            builder1.append(String.format("Request uri %s %s \n", req.uri(), req.method()));
            req.headers().entries().forEach(e ->
                    builder1.append(String.format("- hdr %s : %s \n", e.getKey(), e.getValue())));
            logger.info(builder1.toString());
            req.endHandler(x -> {
                StringBuilder builder2 = new StringBuilder();
                builder2.append(String.format("Response %s \n", req.response().getStatusCode()));
                req.response().headers().entries().forEach(e ->
                        builder2.append(String.format("- hdr %s : %s \n", e.getKey(), e.getValue())));
                logger.info(builder2.toString());
            });
            next.accept(req, arg);
        };
    }
}
