package com.github.spriet2000.vertx.handlers.http.server.ext.statik.impl;

import com.github.spriet2000.vertx.handlers.http.server.ext.statik.StatikFileHandlerException;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class StatikFileHandler<A> implements BiFunction<BiConsumer<HttpServerRequest, Throwable>, BiConsumer<HttpServerRequest, A>,
        BiConsumer<HttpServerRequest, A>> {

    private final String appRoot;
    private final String defaultFile;


    public StatikFileHandler(String appRoot) {
        this.appRoot = appRoot;
        this.defaultFile = "index.html";
    }

    public StatikFileHandler(String appRoot, String defaultFile) {
        this.appRoot = appRoot;
        this.defaultFile = defaultFile;
    }

    @Override
    public BiConsumer<HttpServerRequest, A> apply(BiConsumer<HttpServerRequest, Throwable> fail,
                                                  BiConsumer<HttpServerRequest, A> next) {
        return (req, arg) -> {
            if (req.method() != HttpMethod.GET && req.method() != HttpMethod.HEAD) {
                next.accept(req, arg);
            } else if (req.path().equals("/")) {
                sendFile(req, fail, next, arg,
                        String.format("%s%s%s", appRoot, File.separator, defaultFile));

            } else if (!req.path().contains("..")) {
                sendFile(req, fail, next, arg,
                        String.format("%s%s%s", appRoot, File.separator, req.path()));
            }
        };
    }

    @SuppressWarnings("unchecked")
    private void sendFile(HttpServerRequest req, BiConsumer fail, BiConsumer next, A arg, String path) {
        req.response().sendFile(path, event -> {
            if (event.failed()) {
                fail.accept(req, new StatikFileHandlerException(event.cause()));
            } else {
                next.accept(req, arg);
            }
        });
    }
}

