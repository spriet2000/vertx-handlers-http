package com.github.spriet2000.vertx.handlers.http.server;

import com.github.spriet2000.vertx.handlers.Handleable;
import com.github.spriet2000.vertx.handlers.Handlers;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;


public final class RequestHandlers implements Handleable<Request> {

    private Handlers<Request> requestHandlers;

    public RequestHandlers(Handler<Throwable> exceptionHandler, Handler<Object> successHandler) {
        requestHandlers = new Handlers<>(exceptionHandler, successHandler);
    }

    public RequestHandlers(Handleable<Request> handleable) {
        requestHandlers = new Handlers<>(handleable.exceptionHandler(), handleable.successHandler());
        for (BiFunction<Handler<Throwable>, Handler<Object>, Handler<Request>> handler : handleable.handlers()) {
            requestHandlers.handlers().add(handler);
        }
    }

    public RequestHandlers handlers(BiFunction<Handler<Throwable>, Handler<Object>, Handler<Request>>... handlers) {
        for (BiFunction<Handler<Throwable>, Handler<Object>, Handler<Request>> handler : handlers) {
            requestHandlers.handlers().add(handler);
        }
        return this;
    }

    public void handle(HttpServerRequest request, Function<HttpServerRequest, Request> factory) {
          requestHandlers.handle(factory.apply(request));
    }

    @Override
    public List<BiFunction<Handler<Throwable>, Handler<Object>, Handler<Request>>> handlers() {
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

    public RequestHandlers then(BiFunction<Handler<Throwable>, Handler<Object>, Handler<Request>> handler){
        requestHandlers.then(handler);
        return this;
    }

    @SafeVarargs
    public final RequestHandlers then(BiFunction<Handler<Throwable>, Handler<Object>, Handler<Request>>... handlers){
        for (BiFunction<Handler<Throwable>, Handler<Object>, Handler<Request>> handler : handlers) {
            then(handler);
        }
        return this;
    }
}
