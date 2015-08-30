package com.github.spriet2000.vertx.handlers.http.server;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;


public final class RequestHandlers<E, A> {

    private com.github.spriet2000.handlers.Handlers requestHandlers;

    public RequestHandlers(BiConsumer<Object, Throwable> exceptionHandler, BiConsumer<Object, A> successHandler) {
        requestHandlers = new com.github.spriet2000.handlers.Handlers(exceptionHandler, successHandler);
    }

    public RequestHandlers<E, A> handlers(BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<E, A>>... handlers) {
        requestHandlers.andThen(handlers);
        return this;
    }

    public void handle(E request, Object arg) {
        requestHandlers.accept(request, arg);
    }

    public List<BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<E, Object>>> handlers() {
        return requestHandlers.handlers();
    }

    public RequestHandlers<E, A> andThen(BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<E, A>>... handlers) {
        requestHandlers.andThen(handlers);
        return this;
    }
}
