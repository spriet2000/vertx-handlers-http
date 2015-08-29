package com.github.spriet2000.vertx.handlers.http.server;

import com.github.spriet2000.vertx.handlers.Handleable;
import com.github.spriet2000.vertx.handlers.Handlers;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;


public final class RequestHandlers<E> implements Handler<HttpServerRequest>, Handleable<E> {

    private Handlers<E> requestHandlers;

    public static RequestHandlers handlers(RequestHandlers handlers) {
        return new RequestHandlers(handlers);
    }

    public RequestHandlers(Handler<Throwable> exceptionHandler, Handler<Object> successHandler) {
        requestHandlers = new Handlers<>(exceptionHandler, successHandler);
    }

    public RequestHandlers(Handleable<E> handleable) {
        requestHandlers = new Handlers<>(handleable.exceptionHandler(), handleable.successHandler());
        for (BiFunction<Handler<Throwable>, Handler<Object>, Handler<E>> handler : handleable.handlers()) {
            requestHandlers.handlers().add(handler);
        }
    }

    public RequestHandlers handlers(BiFunction<Handler<Throwable>, Handler<Object>, Handler<E>>... handlers) {
        for (BiFunction<Handler<Throwable>, Handler<Object>, Handler<E>> handler : handlers) {
            requestHandlers.handlers().add(handler);
        }
        return this;
    }

    public void handle(HttpServerRequest request, Function<HttpServerRequest, E> factory) {
          requestHandlers.handle(factory.apply(request));
    }

    public void handle(HttpServerRequest request) {
        requestHandlers.handle((E) request);
    }

    @Override
    public List<BiFunction<Handler<Throwable>, Handler<Object>, Handler<E>>> handlers() {
        return requestHandlers.handlers();
    }

    @Override
    public Handler<Throwable> exceptionHandler() {
        return requestHandlers.exceptionHandler();
    }

    @Override
    public Handler<Object> successHandler() {
        return requestHandlers.successHandler();
    }

    public RequestHandlers<E> then(BiFunction<Handler<Throwable>, Handler<Object>, Handler<E>> handler){
        requestHandlers.then(handler);
        return this;
    }

    @SafeVarargs
    public final RequestHandlers<E> then(BiFunction<Handler<Throwable>, Handler<Object>, Handler<E>>... handlers){
        for (BiFunction<Handler<Throwable>, Handler<Object>, Handler<E>> handler : handlers) {
            then(handler);
        }
        return this;
    }
}
