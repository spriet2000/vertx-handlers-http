package com.github.spriet2000.vertx.handlers.http.handlers.bodyParser;


public interface Body<T> {
    void body(T body);

    T body();
}
