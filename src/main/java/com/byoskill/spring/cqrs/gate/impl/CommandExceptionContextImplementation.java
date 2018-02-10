/**
 * Copyright (C) 2017-2018 Credifix
 */
package com.byoskill.spring.cqrs.gate.impl;

import com.byoskill.spring.cqrs.api.ICommandHandler;
import com.byoskill.spring.cqrs.gate.api.ICommandExceptionContext;

final class CommandExceptionContextImplementation implements ICommandExceptionContext {
    private final Object			 command;
    private final Throwable			 e;
    private final ICommandHandler<?, ?> handler;

    /**
     * Instantiates a new command exception context implementation.
     *
     * @param command the command
     * @param e the e
     * @param handler the handler
     */
    public CommandExceptionContextImplementation(final Object command, final Throwable e,
	    final ICommandHandler<?, ?> handler) {
	this.command = command;
	this.e = e;
	this.handler = handler;
    }

    @Override
    public Object getCommand() {
	return command;
    }

    @Override
    public Throwable getException() {

	return e;
    }

    @Override
    public Object getHandler() {
	return handler;
    }
}