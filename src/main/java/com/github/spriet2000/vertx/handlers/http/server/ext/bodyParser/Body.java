package com.github.spriet2000.vertx.handlers.http.server.ext.bodyParser;


public interface Body<T> {
    void body(T body);

    T body();
}
