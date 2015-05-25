package com.github.spriet2000.vertx.handlers.http.server.ext.impl;

import com.github.spriet2000.vertx.handlers.http.server.ServerHandler;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

public class ErrorResponse implements ServerHandler<Object> {

    public static String template = "<html><head></head><body><h1>%s %s</h1></body></html>";

    public static ErrorResponse errorResponse() {
        return new ErrorResponse();
    }

    public void handle(HttpServerRequest req, HttpServerResponse res, Object args) {
        Integer code = args instanceof Integer ? (Integer) args : Integer.valueOf(500);
        String body = String.format(template, String.valueOf(code),
                HttpResponseStatus.valueOf(code).reasonPhrase());
        res.headers().set("content-length", String.valueOf(body.length()));
        res.setStatusCode(code);
        res.end(body);
    }
}
