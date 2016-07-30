package com.github.spriet2000.vertx.handlers.extensions.bodyParser;


public interface Body<T> {
    void body(T body);

    T body();
}
