/**
 * Copyright (C) 2017-2018 Credifix
 */
package com.byoskill.spring.cqrs.gate.impl;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.byoskill.spring.cqrs.api.IAsyncCommandHandler;
import com.byoskill.spring.cqrs.api.ICommandHandler;

public class AsyncCommandHandlerWrapper<T, R> implements IAsyncCommandHandler<T, R> {
    private static final Logger		LOGGER = LoggerFactory.getLogger(AsyncCommandHandlerWrapper.class);
    private final ICommandHandler<T, R>	handler;

    /**
     * Instantiates a new async command handler wrapper.
     *
     * @param handler
     *            the handler
     */
    public AsyncCommandHandlerWrapper(final ICommandHandler<T, R> handler) {
	this.handler = handler;
    }

    /* (non-Javadoc)
     * @see com.byoskill.spring.cqrs.api.IAsyncCommandHandler#handle(java.lang.Object)
     */
    @Override
    public CompletableFuture<R> handle(final T command) {
	return CompletableFuture.supplyAsync(() -> handler.handle(command));
    }
}
