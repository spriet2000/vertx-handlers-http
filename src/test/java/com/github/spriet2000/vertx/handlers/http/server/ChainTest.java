package com.github.spriet2000.vertx.handlers.http.server;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.test.core.HttpTestBase;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class ChainTest extends HttpTestBase {

    @Test
    public void testNextHandler() {

        ServerHandlers handlers = ServerHandlers.handlers(new TestHandler(0), new TestHandler(1), new TestHandler(2))
                .completeHandler(testContext(ctx -> {
                    verify(ctx, times(3)).next(anyInt());
                    verify(ctx, never()).fail(anyInt());
                }));

        ServerHandlers.handlers(new InitTestHandler())
                .then(handlers)
                .handle(mock(HttpServerRequest.class), spy(new Context()));
    }

    @Test
    public void testFailHandler1() {

        ServerHandlers handlers = ServerHandlers.handlers(new TestHandler(0), new TestHandler(1, true), new TestHandler(2))
                .completeHandler(testContext(ctx -> {
                    verify(ctx, times(1)).next(anyInt());
                    verify(ctx, times(1)).fail(anyInt());
                }));

        ServerHandlers.handlers(new InitTestHandler())
                .then(handlers)
                .handle(mock(HttpServerRequest.class), spy(new Context()));
    }

    @Test
    public void testFailHandler2() {

        ServerHandlers handlers1 = ServerHandlers.handlers(new TestHandler(1))
                .completeHandler(testContext(ctx -> {
                    verify(ctx, times(1)).next(anyInt());
                    verify(ctx, never()).fail(anyInt());
                }));

        ServerHandlers handlers2 = ServerHandlers.handlers(new TestHandler(2), new TestHandler(2, true))
                .completeHandler(testContext(ctx -> {
                    verify(ctx, times(1)).next(anyInt());
                    verify(ctx, times(1)).fail(anyInt());
                }));

        ServerHandlers handlers3 = ServerHandlers.handlers(new InitTestHandler())
                .then(handlers1)
                .then(handlers2)
                .completeHandler(testContext(ctx -> {
                    verify(ctx, times(2)).next(anyInt());
                    verify(ctx, times(1)).fail(anyInt());
                }));

        handlers1.handle(mock(HttpServerRequest.class), spy(new Context()));
        handlers2.handle(mock(HttpServerRequest.class), spy(new Context()));
        handlers3.handle(mock(HttpServerRequest.class), spy(new Context()));
    }

    @Test
    public void testCompleteHandler() {

        ServerHandlers handlers1 = ServerHandlers.handlers(new TestHandler(1))
                .completeHandler(testContext(ctx -> verify(ctx, times(1)).next(anyInt())));

        ServerHandlers handlers2 = ServerHandlers.handlers(new TestHandler(2), new TestHandler(2))
                .completeHandler(testContext(ctx -> verify(ctx, times(2)).next(anyInt())));

        ServerHandlers handlers3 = ServerHandlers.handlers()
                .then(handlers1)
                .then(handlers2)
                .completeHandler(testContext(ctx -> verify(ctx, times(3)).next(anyInt())));

        ServerHandlers.handlers(new InitTestHandler())
                .then(handlers1)
                .handle(mock(HttpServerRequest.class), spy(new Context()));

        ServerHandlers.handlers(new InitTestHandler())
                .then(handlers2)
                .handle(mock(HttpServerRequest.class), spy(new Context()));

        ServerHandlers.handlers(new InitTestHandler())
                .then(handlers3)
                .handle(mock(HttpServerRequest.class), spy(new Context()));
    }

    @Test
    public void testOneHandler() {
        ServerHandlers handlers = ServerHandlers.handlers(new TestHandler(0))
                .completeHandler(testContext(ctx -> verify(ctx, only()).next(0)));
        handlers.handle(mock(HttpServerRequest.class), mock(Context.class));
    }

    @Test
    public void testTwoHandlers() {
        ServerHandlers handlers = ServerHandlers.handlers(new TestHandler(0), new TestHandler(1))
                .completeHandler(testContext(ctx -> {
                    InOrder inOrder = ctx.inOrder();
                    inOrder.verify(ctx).next(0);
                    inOrder.verify(ctx).next(1);
                    verify(ctx, times(2)).next(anyInt());
                }));
        ServerHandlers.handlers(new InitTestHandler())
                .then(handlers)
                .handle(mock(HttpServerRequest.class), spy(new Context()));
    }

    @Test
    public void testMergeHandlers1() {
        ServerHandlers handlers1 = ServerHandlers.handlers(new TestHandler(0), new TestHandler(1));
        ServerHandlers handlers2 = ServerHandlers.handlers(new TestHandler(2), new TestHandler(3));
        ServerHandlers handlers3 = ServerHandlers.handlers(new TestHandler(-1))
                .then(handlers1)
                .then(handlers2)
                .completeHandler(testContext(ctx -> {
                    InOrder inOrder = ctx.inOrder();
                    inOrder.verify(ctx).next(-1);
                    inOrder.verify(ctx).next(0);
                    inOrder.verify(ctx).next(1);
                    inOrder.verify(ctx).next(2);
                    inOrder.verify(ctx).next(3);
                    verify(ctx, times(5)).next(anyInt());
                }));
        ServerHandlers.handlers(new InitTestHandler())
                .then(handlers3)
                .handle(mock(HttpServerRequest.class), spy(new Context()));
    }

    @Test
    public void testMergeMixHandlers1() {

        ServerHandlers handlers1 = ServerHandlers.handlers(new TestHandler(0), new TestHandler(1))
                .completeHandler(testContext(ctx -> {
                    InOrder order = ctx.inOrder();
                    order.verify(ctx).next(0);
                    order.verify(ctx).next(1);
                    verify(ctx, times(2)).next(anyInt());
                }));

        ServerHandlers handlers2 = ServerHandlers.handlers(new TestHandler(2), new TestHandler(3));
        ServerHandlers handlers3 = ServerHandlers.handlers(new TestHandler(-1))
                .then(handlers1)
                .then(handlers2)
                .completeHandler(testContext(ctx -> {
                    InOrder inOrder = ctx.inOrder();
                    inOrder.verify(ctx).next(-1);
                    inOrder.verify(ctx).next(0);
                    inOrder.verify(ctx).next(1);
                    inOrder.verify(ctx).next(2);
                    inOrder.verify(ctx).next(3);
                    verify(ctx, times(5)).next(anyInt());
                }));

        ServerHandlers.handlers(new InitTestHandler())
                .then(handlers1)
                .handle(mock(HttpServerRequest.class), spy(new Context()));

        ServerHandlers.handlers(new InitTestHandler())
                .then(handlers3)
                .handle(mock(HttpServerRequest.class), spy(new Context()));
    }

    ServerHandler<Context> testContext(Handler<Context> handler) {
        return (req, res, ctx) -> handler.handle(ctx);
    }

    class InitTestHandler implements ServerController {

        @Override
        public ServerHandler<Context> handle(Handler fail, Handler next) {
            return (req, res, ctx) -> {
                if (ctx != null) {
                    ctx.inOrder(inOrder(ctx));
                }
                next.handle(ctx);
            };
        }
    }

    class TestHandler implements ServerController {

        private final int value;
        private final boolean failHandler;

        public TestHandler(int value) {

            this.value = value;
            this.failHandler = false;
        }

        public TestHandler(int value, boolean fail) {

            this.value = value;
            this.failHandler = fail;
        }

        @Override
        public ServerHandler<Context> handle(Handler fail, Handler next) {
            return (req, res, ctx) -> {
                if (failHandler) {
                    ctx.fail(value);
                    fail.handle(ctx);
                } else {
                    ctx.next(value);
                    next.handle(ctx);
                }
            };
        }
    }

    class Context {
        InOrder inOrder;

        public void next(int value) {
            return;
        }

        public void fail(int value) {
            return;
        }

        public InOrder inOrder() {
            return inOrder;
        }

        public void inOrder(InOrder inOrder) {
            this.inOrder = inOrder;
        }
    }
}
