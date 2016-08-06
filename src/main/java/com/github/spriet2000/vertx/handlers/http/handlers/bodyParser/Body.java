package com.github.spriet2000.vertx.handlers.http.handlers.bodyParser;


public interface Body<T> {
    void setBody(T body);

    T getBody();
}
