package com.github.spriet2000.vertx.handlers.core.tests;

import com.github.spriet2000.vertx.handlers.core.BiHandlers;
import com.github.spriet2000.vertx.handlers.core.impl.BiHandlersImpl;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("ALL")
public class BiHandlersTest {

    @Test
    public void testSuccess() {

        AtomicBoolean hitException = new AtomicBoolean(false);
        AtomicBoolean hitComplete = new AtomicBoolean(false);

        BiHandlers<StringBuilder, Void> handlers = new BiHandlersImpl<>();
        handlers.andThen(
                (f, n) -> (e, a) -> {
                    e.append("1");
                    n.accept(e, a);
                }, (f, n) -> (e, a) -> {
                    e.append("2");
                    n.accept(e, a);
                }, (f, n) -> (e, a) -> {
                    e.append("3");
                    n.accept(e, a);
                });

        BiConsumer<StringBuilder, Void> handler = handlers.apply(
                (e, a) -> hitException.set(true),
                (e, a) -> hitComplete.set(true));

        StringBuilder builder1 = new StringBuilder();

        handler.accept(builder1, null);

        assertEquals("123", builder1.toString());

        StringBuilder builder2 = new StringBuilder();

        handler.accept(builder2, null);

        assertEquals("123", builder2.toString());

        assertEquals(false, hitException.get());
        assertEquals(true, hitComplete.get());
    }

    @Test
    public void testException() {

        AtomicBoolean hitException = new AtomicBoolean(false);
        AtomicBoolean hitComplete = new AtomicBoolean(false);

        BiHandlers<StringBuilder, Void> handlers = new BiHandlersImpl<>();
        handlers.andThen(
                (f, n) -> n::accept,
                (f, n) -> (e, a) -> f.accept(e, new RuntimeException()),
                (f, n) -> n::accept);

        BiConsumer<StringBuilder, Void> handler = handlers.apply(
                (e, a) -> hitException.set(true),
                (e, a) -> hitComplete.set(true));

        handler.accept(null, null);

        assertEquals(true, hitException.get());
        assertEquals(false, hitComplete.get());
    }

    public class ExampleHandler implements BiFunction<BiConsumer<StringBuilder, Throwable>,
            BiConsumer<StringBuilder, Void>, BiConsumer<StringBuilder, Void>> {

        @Override
        public BiConsumer<StringBuilder, Void> apply(BiConsumer<StringBuilder, Throwable> fail,
                                                     BiConsumer<StringBuilder, Void> next) {
            return next;
        }
    }

}