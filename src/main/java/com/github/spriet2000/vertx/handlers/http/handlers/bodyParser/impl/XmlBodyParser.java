package com.github.spriet2000.vertx.handlers.http.handlers.bodyParser.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.spriet2000.vertx.handlers.http.handlers.bodyParser.Body;
import com.github.spriet2000.vertx.handlers.http.handlers.bodyParser.BodyParsingException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

public class XmlBodyParser<A extends Body> implements BiFunction<BiConsumer<HttpServerRequest, Throwable>, BiConsumer<HttpServerRequest, A>,
        BiConsumer<HttpServerRequest, A>> {

    private final Class clazz;

    public XmlBodyParser(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public BiConsumer<HttpServerRequest, A> apply(BiConsumer<HttpServerRequest, Throwable> fail,
                                                  BiConsumer<HttpServerRequest, A> next) {
        return (req, arg) -> {
            if (req.headers().contains(CONTENT_TYPE)
                    && !req.headers().get(CONTENT_TYPE).equals("application/xml")) {
                next.accept(req, arg);
                return;
            }
            if (req.method() == HttpMethod.GET
                    || req.method() == HttpMethod.HEAD) {
                next.accept(req, arg);
            } else {
                Buffer body = Buffer.buffer();
                req.handler(body::appendBuffer);
                req.endHandler(e -> {
                    ObjectMapper mapper = new XmlMapper();
                    try {
                        arg.setBody(mapper.readValue(body.toString(), clazz));
                        next.accept(req, arg);
                    } catch (Exception exception) {
                        fail.accept(req, new BodyParsingException(exception));
                    }
                });
            }
        };
    }
}
