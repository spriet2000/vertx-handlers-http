package com.github.spriet2000.vertx.handlers.core.http;

import io.vertx.core.http.HttpServerRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ServerRequestHandlers<A> {

    private List<BiFunction<BiConsumer<HttpServerRequest, Throwable>, BiConsumer<HttpServerRequest, A>, BiConsumer<HttpServerRequest, A>>> handlers = new ArrayList<>();

    public ServerRequestHandlers() {
    }

    @SafeVarargs
    public ServerRequestHandlers(BiFunction<BiConsumer<HttpServerRequest, Throwable>, BiConsumer<HttpServerRequest, A>, BiConsumer<HttpServerRequest, A>>... handlers) {
        Collections.addAll(this.handlers, handlers);
    }

    @SafeVarargs
    public ServerRequestHandlers(ServerRequestHandlers<A>... handlers) {
        for (ServerRequestHandlers<A> handler : handlers) {
            this.handlers.addAll(handler.handlers.stream().collect(Collectors.toList()));
        }
    }

    @SafeVarargs
    public static <A> ServerRequestHandlers<A> build(ServerRequestHandlers<A>... handlers) {
        return new ServerRequestHandlers<>(handlers);
    }

    @SafeVarargs
    public static <A> ServerRequestHandlers<A> build(BiFunction<BiConsumer<HttpServerRequest, Throwable>, BiConsumer<HttpServerRequest, A>, BiConsumer<HttpServerRequest, A>>... handlers) {
        return new ServerRequestHandlers<>(handlers);
    }

    public BiConsumer<HttpServerRequest, A> apply(BiConsumer<HttpServerRequest, Throwable> exceptionHandler, BiConsumer<HttpServerRequest, A> successHandler) {
        BiConsumer<HttpServerRequest, A> last = successHandler;
        for (int i = handlers.size() - 1; i >= 0; i--) {
            final BiConsumer<HttpServerRequest, A> previous = last;
            last = handlers.get(i).apply(
                    exceptionHandler,
                    previous);
        }
        return last;
    }

    @SafeVarargs
    public final ServerRequestHandlers<A> andThen(BiConsumer<HttpServerRequest, A>... consumers) {
        for (BiConsumer<HttpServerRequest, A> consumer : consumers) {
            handlers.add((f, n) -> consumer);
        }
        return this;
    }

    @SafeVarargs
    public final ServerRequestHandlers<A> andThen(BiFunction<BiConsumer<HttpServerRequest, Throwable>, BiConsumer<HttpServerRequest, A>, BiConsumer<HttpServerRequest, A>>... handlers) {
        Collections.addAll(this.handlers, handlers);
        return this;
    }

    @SafeVarargs
    public final ServerRequestHandlers<A> andThen(ServerRequestHandlers<A>... handlers) {
        for (ServerRequestHandlers<A> handler : handlers) {
            this.handlers.addAll(handler.handlers.stream().collect(Collectors.toList()));
        }
        return this;
    }
}