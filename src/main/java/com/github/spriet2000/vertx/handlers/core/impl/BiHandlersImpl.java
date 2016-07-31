package com.github.spriet2000.vertx.handlers.core.impl;

import com.github.spriet2000.vertx.handlers.core.BiHandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public final class BiHandlersImpl<E, A> implements BiHandlers<E, A> {

    private List<BiFunction<BiConsumer<E, Throwable>, BiConsumer<E, A>, BiConsumer<E, A>>> handlers;

    public BiHandlersImpl() {
    }

    @Override
    public BiConsumer<E, A> apply(BiConsumer<E, Throwable> exceptionHandler, BiConsumer<E, A> successHandler) {
        BiConsumer<E, A> last = successHandler;
        for (int i = handlers.size() - 1; i >= 0; i--) {
            final BiConsumer<E, A> previous = last;
            last = handlers.get(i).apply(
                    exceptionHandler,
                    previous);
        }
        return last;
    }

    @Override
    @SafeVarargs
    public final BiHandlersImpl<E, A> andThen(BiConsumer<E, A>... consumers) {
        for (BiConsumer<E, A> consumer : consumers) {
            handlers().add((f, n) -> consumer);
        }
        return this;
    }

    @Override
    @SafeVarargs
    public final BiHandlersImpl<E, A> andThen(BiFunction<BiConsumer<E, Throwable>, BiConsumer<E, A>, BiConsumer<E, A>>... handlers) {
        Collections.addAll(handlers(), handlers);
        return this;
    }

    @Override
    @SafeVarargs
    public final BiHandlersImpl<E, A> andThen(BiHandlersImpl<E, A>... handlers) {
        for (BiHandlersImpl<E, A> handler : handlers) {
            handlers().addAll(handler.handlers().stream().collect(Collectors.toList()));
        }
        return this;
    }

    @Override
    public List<BiFunction<BiConsumer<E, Throwable>, BiConsumer<E, A>, BiConsumer<E, A>>> handlers() {
        if (handlers == null) {
            handlers = new ArrayList<>();
        }
        return handlers;
    }
}