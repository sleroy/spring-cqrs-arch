package com.byoskill.spring.cqrs.gate.impl;

import java.util.concurrent.CompletableFuture;

import com.byoskill.spring.cqrs.api.IAsyncCommandHandler;
import com.byoskill.spring.cqrs.api.ICommandCallback;

/**
 * Default callback to invoke sequentially a command.
 *
 * @author sleroy
 *
 * @param <R>
 */
public class DefaultCommandCallback<R> implements ICommandCallback<R> {

    private final Object			  command;
    private final IAsyncCommandHandler<Object, R> handler;

    public DefaultCommandCallback(final Object _command, final IAsyncCommandHandler<Object, R> _handler) {
	command = _command;
	handler = _handler;
    }

    @Override
    public CompletableFuture<R> call() throws Exception {

	return handler.handle(command);
    }

}
