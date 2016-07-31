package com.github.spriet2000.vertx.handlers.core;

import com.github.spriet2000.vertx.handlers.core.impl.BiHandlersImpl;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;


public interface BiHandlers<E, A> {

    BiConsumer<E, A> apply(BiConsumer<E, Throwable> exceptionHandler, BiConsumer<E, A> successHandler);

    BiHandlersImpl<E, A> andThen(BiConsumer<E, A>... consumers);

    BiHandlersImpl<E, A> andThen(BiFunction<BiConsumer<E, Throwable>, BiConsumer<E, A>, BiConsumer<E, A>>... handlers);

    BiHandlersImpl<E, A> andThen(BiHandlersImpl<E, A>... handlers);

    List<BiFunction<BiConsumer<E, Throwable>, BiConsumer<E, A>, BiConsumer<E, A>>> list();
}
