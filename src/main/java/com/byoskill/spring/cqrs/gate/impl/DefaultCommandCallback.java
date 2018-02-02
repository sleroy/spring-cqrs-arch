/**
 * Copyright (C) 2017-2018 Credifix
 */
package com.byoskill.spring.cqrs.gate.impl;

import java.util.concurrent.CompletableFuture;

import com.byoskill.spring.cqrs.api.IAsyncCommandHandler;
import com.byoskill.spring.cqrs.api.ICommandCallback;

/**
 * Default callback to invoke sequentially a command.
 *
 * @author sleroy
 * @param <R> the generic type
 */
public class DefaultCommandCallback<R> implements ICommandCallback<R> {

    private final Object			  command;
    private final IAsyncCommandHandler<Object, R> handler;

    /**
     * Instantiates a new default command callback.
     *
     * @param _command the command
     * @param _handler the handler
     */
    public DefaultCommandCallback(final Object _command, final IAsyncCommandHandler<Object, R> _handler) {
	command = _command;
	handler = _handler;
    }

    @Override
    public CompletableFuture<R> call() throws Exception {

	return handler.handle(command);
    }

}
