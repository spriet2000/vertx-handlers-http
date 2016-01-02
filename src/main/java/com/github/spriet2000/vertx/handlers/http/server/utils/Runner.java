package com.github.spriet2000.vertx.handlers.http.server.utils;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.nio.file.Paths;
import java.util.function.Consumer;

public final class Runner {

    public static void run(Class clazz, VertxOptions options) {
        run(Paths.get("").toAbsolutePath().toString(), clazz.getName(), options);
    }

    public static void run(String dir, Class clazz, VertxOptions options) {
        run(dir, clazz.getName(), options);
    }

    public static void run(String dir, String verticle, VertxOptions options) {

        System.setProperty("vertx.cwd", dir);

        Consumer<Vertx> runner = vertx -> {
            try {
                vertx.deployVerticle(verticle);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        };
        if (options.isClustered()) {
            Vertx.clusteredVertx(options, res -> {
                if (res.succeeded()) {
                    Vertx vertx = res.result();
                    runner.accept(vertx);
                } else {
                    res.cause().printStackTrace();
                }
            });
        } else {
            Vertx vertx = Vertx.vertx(options);
            runner.accept(vertx);
        }
    }
}
