package com.github.spriet2000.vertx.handlers.http.server;

import io.vertx.core.http.HttpServerRequest;

public class RequestContext implements Request {

    private final HttpServerRequest request;

    public RequestContext(HttpServerRequest request){

        this.request = request;
    }

    public HttpServerRequest request() {
        return request;
    }
}

