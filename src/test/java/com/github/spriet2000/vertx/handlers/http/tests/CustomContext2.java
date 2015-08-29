package com.github.spriet2000.vertx.handlers.http.tests;

import com.github.spriet2000.vertx.handlers.http.server.RequestContext;
import io.vertx.core.http.HttpServerRequest;

public class CustomContext2 implements RequestContext {

    private final HttpServerRequest request;

    public CustomContext2(HttpServerRequest request){

        this.request = request;
    }

    public HttpServerRequest request() {
        return request;
    }
}

