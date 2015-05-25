package com.github.spriet2000.vertx.handlers.http.server.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;

import com.github.spriet2000.vertx.handlers.Handlers;

public class HttpServerResponseImpl implements HttpServerResponse {

    private final HttpServerResponse response;
    private final Handlers closeHandlers  = new Handlers<>();
    private final Handlers headersEndHandlers = new Handlers<>();
    private final Handlers bodyEndHandlers = new Handlers<>();

    public HttpServerResponseImpl(HttpServerResponse response) {

        this.response = response;
    }

    @Override
    public HttpServerResponse exceptionHandler(Handler<Throwable> handler) {
        return response.exceptionHandler(handler);
    }

    @Override
    public HttpServerResponse write(Buffer data) {
        return response.write(data);
    }

    @Override
    public HttpServerResponse setWriteQueueMaxSize(int maxSize) {
        return response.setWriteQueueMaxSize(maxSize);
    }

    @Override
    public boolean writeQueueFull() {
        return response.writeQueueFull();
    }

    @Override
    public HttpServerResponse drainHandler(Handler<Void> handler) {
        return response.drainHandler(handler);
    }

    @Override
    public int getStatusCode() {
        return response.getStatusCode();
    }

    @Override
    public HttpServerResponse setStatusCode(int statusCode) {
        return response.setStatusCode(statusCode);
    }

    @Override
    public String getStatusMessage() {
        return response.getStatusMessage();
    }

    @Override
    public HttpServerResponse setStatusMessage(String statusMessage) {
        return response.setStatusMessage(statusMessage);
    }

    @Override
    public HttpServerResponse setChunked(boolean chunked) {
        return response.setChunked(chunked);
    }

    @Override
    public boolean isChunked() {
        return response.isChunked();
    }

    @Override
    public MultiMap headers() {
        return response.headers();
    }

    @Override
    public HttpServerResponse putHeader(String name, String value) {
        return response.putHeader(name, value);
    }

    @Override
    public HttpServerResponse putHeader(CharSequence name, CharSequence value) {
        return response.putHeader(name, value);
    }

    @Override
    public HttpServerResponse putHeader(String name, Iterable<String> values) {
        return response.putHeader(name, values);
    }

    @Override
    public HttpServerResponse putHeader(CharSequence name, Iterable<CharSequence> values) {
        return response.putHeader(name, values);
    }

    @Override
    public MultiMap trailers() {
        return response.trailers();
    }

    @Override
    public HttpServerResponse putTrailer(String name, String value) {
        return response.putTrailer(name, value);
    }

    @Override
    public HttpServerResponse putTrailer(CharSequence name, CharSequence value) {
        return response.putTrailer(name, value);
    }

    @Override
    public HttpServerResponse putTrailer(String name, Iterable<String> values) {
        return response.putTrailer(name, values);
    }

    @Override
    public HttpServerResponse putTrailer(CharSequence name, Iterable<CharSequence> value) {
        return response.putTrailer(name, value);
    }

    @Override
    public HttpServerResponse closeHandler(Handler<Void> handler) {
        return response.closeHandler(closeHandlers.then(handler));
    }

    @Override
    public HttpServerResponse write(String chunk, String enc) {
        return response.write(chunk, enc);
    }

    @Override
    public HttpServerResponse write(String chunk) {
        return response.write(chunk);
    }

    @Override
    public void end(String chunk) {
        response.end(chunk);
    }

    @Override
    public void end(String chunk, String enc) {
        response.end(chunk, enc);
    }

    @Override
    public void end(Buffer chunk) {
        response.end(chunk);
    }

    @Override
    public void end() {
        response.end();
    }

    @Override
    public HttpServerResponse sendFile(String filename) {
        return response.sendFile(filename);
    }

    @Override
    public HttpServerResponse sendFile(String filename, Handler<AsyncResult<Void>> resultHandler) {
        return response.sendFile(filename, resultHandler);
    }

    @Override
    public void close() {
        response.close();
    }

    @Override
    public boolean ended() {
        return response.ended();
    }

    @Override
    public boolean headWritten() {
        return response.headWritten();
    }

    @Override
    public HttpServerResponse headersEndHandler(Handler<Void> handler) {
        return response.headersEndHandler(headersEndHandlers.then(handler));
    }

    @Override
    public HttpServerResponse bodyEndHandler(Handler<Void> handler) {
        return response.bodyEndHandler(bodyEndHandlers.then(handler));
    }
}
