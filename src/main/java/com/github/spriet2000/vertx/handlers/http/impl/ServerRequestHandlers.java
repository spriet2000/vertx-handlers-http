package com.github.spriet2000.vertx.handlers.http.impl;

import com.github.spriet2000.vertx.handlers.core.BiHandlers;
import com.github.spriet2000.vertx.handlers.core.impl.BiHandlersImpl;
import io.vertx.core.http.HttpServerRequest;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ServerRequestHandlers<A> implements BiHandlers<HttpServerRequest, A> {

    private BiHandlersImpl<HttpServerRequest, A> biHandlers = new BiHandlersImpl<>();

    public ServerRequestHandlers() {
    }

    @SafeVarargs
    public ServerRequestHandlers(BiFunction<BiConsumer<HttpServerRequest, Throwable>, BiConsumer<HttpServerRequest, A>,
            BiConsumer<HttpServerRequest, A>>... handlers) {
        Collections.addAll(biHandlers.getList(), handlers);
    }

    @SafeVarargs
    public ServerRequestHandlers(ServerRequestHandlers<A>... handlers) {
        for (ServerRequestHandlers<A> handler : handlers) {
            biHandlers.getList().addAll(handler.getList().stream().collect(Collectors.toList()));
        }
    }

    @Override
    public BiConsumer<HttpServerRequest, A> apply(BiConsumer<HttpServerRequest, Throwable> exceptionHandler,
                                                  BiConsumer<HttpServerRequest, A> successHandler) {
        return biHandlers.apply(exceptionHandler, successHandler);
    }

    @SafeVarargs
    @Override
    public final BiHandlersImpl<HttpServerRequest, A> andThen(BiConsumer<HttpServerRequest, A>... biConsumers) {
        return biHandlers.andThen(biConsumers);
    }

    @SafeVarargs
    @Override
    public final BiHandlersImpl<HttpServerRequest, A> andThen(BiFunction<BiConsumer<HttpServerRequest, Throwable>,
            BiConsumer<HttpServerRequest, A>, BiConsumer<HttpServerRequest, A>>... list) {
        return biHandlers.andThen(list);
    }

    @SafeVarargs
    @Override
    public final BiHandlersImpl<HttpServerRequest, A> andThen(BiHandlersImpl<HttpServerRequest, A>... biHandlers) {
        return this.biHandlers.andThen(biHandlers);
    }

    @Override
    public List<BiFunction<BiConsumer<HttpServerRequest, Throwable>, BiConsumer<HttpServerRequest, A>,
            BiConsumer<HttpServerRequest, A>>> getList() {
        return biHandlers.getList();
    }

    @SafeVarargs
    public static <A> ServerRequestHandlers<A> use(ServerRequestHandlers<A>... handlers) {
        return new ServerRequestHandlers<>(handlers);
    }

    @SafeVarargs
    public static <A> ServerRequestHandlers<A> use(BiFunction<BiConsumer<HttpServerRequest, Throwable>,
            BiConsumer<HttpServerRequest, A>, BiConsumer<HttpServerRequest, A>>... handlers) {
        return new ServerRequestHandlers<>(handlers);
    }

}