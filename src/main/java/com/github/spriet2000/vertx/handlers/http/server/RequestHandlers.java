package com.github.spriet2000.vertx.handlers.http.server;

import com.github.spriet2000.handlers.Handlers;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;


public final class RequestHandlers<E> {

    private Handlers<E> requestHandlers;

    public RequestHandlers(BiConsumer<E, Throwable> exceptionHandler, BiConsumer<E, Object> successHandler) {
        requestHandlers = new Handlers<>(exceptionHandler, successHandler);
    }

    public RequestHandlers<E> handlers(BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<E, Object>>... handlers) {
        for (BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<E, Object>> handler : handlers) {
            requestHandlers.andThen(handler);
        }
        return this;
    }

    public void handle(E request, Object arg) {
          requestHandlers.accept(request, arg);
    }

    public List<BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<E, Object>>> handlers() {
        return requestHandlers.handlers();
    }

    public RequestHandlers<E> andThen(BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<E, Object>>... handlers){
        for (BiFunction<Consumer<Throwable>, Consumer<Object>, BiConsumer<E, Object>> handler : handlers) {
            requestHandlers.andThen(handler);
        }
        return this;
    }
}
