package com.github.spriet2000.vertx.handlers.http.server;

import io.vertx.core.http.HttpServerRequest;

public final class RequestContext {

    private final HttpServerRequest request;

    public RequestContext(HttpServerRequest request){

        this.request = request;
    }

    public HttpServerRequest request(){
        return request;
    }
}
