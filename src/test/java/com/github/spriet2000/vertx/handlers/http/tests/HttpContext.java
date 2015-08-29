package com.github.spriet2000.vertx.handlers.http.tests;

import io.vertx.core.http.HttpServerRequest;

public class HttpContext
{
    private final HttpServerRequest request;

    HttpContext(HttpServerRequest request){

        this.request = request;
    }

    public HttpServerRequest request() {
        return request;
    }
}

