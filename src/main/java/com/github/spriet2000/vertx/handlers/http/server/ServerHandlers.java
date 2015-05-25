package com.github.spriet2000.vertx.handlers.http.server;

import com.github.spriet2000.vertx.handlers.Handlers3;
import com.github.spriet2000.vertx.handlers.http.server.impl.HttpServerResponseImpl;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.util.function.Supplier;

public final class ServerHandlers extends Handlers3<HttpServerRequest, HttpServerResponse, Object,
        ServerHandler<Object>,
        ServerController<ServerHandler<Object>>> implements Handler<HttpServerRequest> {

    public ServerHandlers(ServerController... controllers) {
        super(controllers);
    }

    public ServerHandlers(ServerHandlers handlers) {
        super(handlers);
    }

    public static ServerHandlers handlers() {
        return new ServerHandlers();
    }

    public static ServerHandlers handlers(ServerHandlers handlers) {
        ServerHandlers newHandlers = handlers();
        Handlers3.merge(handlers, newHandlers);
        return newHandlers;
    }

    public static ServerHandlers handlers(ServerController... controllers) {
        return handlers().then(controllers);
    }

    public static ServerHandlers handlers(ServerHandler... handlers) {
        ServerHandlers serverHandlers = handlers();
        for (ServerHandler handler : handlers) {
            serverHandlers.then((fail, next) -> ((event1, event2, event3) -> {
                handler.handle(event1,event2,event3);
                next.handle(event3);
            }));
        }
        return serverHandlers;
    }

    public final ServerHandlers then(ServerHandlers handlers) {
        super.then(handlers);
        return this;
    }

    public final ServerHandlers then(ServerController... controllers) {
        super.then(controllers);
        return this;
    }

    public final ServerHandlers then(ServerHandler... handlers) {
        for (ServerHandler handler : handlers) {
            super.then((fail, next) -> ((event1, event2, event3) -> {
                handler.handle(event1,event2,event3);
                next.handle(event3);
            }));
        }
        return this;
    }

    public ServerHandlers with(Supplier<Object> supplier) {
        super.with(supplier);
        return this;
    }

    public Supplier<Object> args() {
        return super.args();
    }

    public ServerHandlers exceptionHandler(ServerHandler exceptionHandler) {
        super.exceptionHandler(exceptionHandler);
        return this;
    }

    public ServerHandlers exceptionHandler(ServerHandlers exceptionHandler) {
        super.exceptionHandler(exceptionHandler.handler());
        return this;
    }

    public ServerHandlers completeHandler(ServerHandler completeHandler) {
        super.completeHandler((ServerHandler) completeHandler);
        return this;
    }

    public ServerHandlers completeHandler(ServerHandlers completeHandler) {
        super.exceptionHandler((ServerHandler) completeHandler.handler());
        return this;
    }

    public void handle(HttpServerRequest request) {
        handle(request, args() != null ? args().get() : null);
    }

    public void handle(HttpServerRequest request, Object arguments) {
        handle(request, new HttpServerResponseImpl(request.response()), arguments);
    }

}
