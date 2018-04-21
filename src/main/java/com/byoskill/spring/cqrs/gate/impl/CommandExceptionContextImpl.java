/**
 * Copyright (C) 2017-2018 Credifix
 */
package com.byoskill.spring.cqrs.gate.impl;

import com.byoskill.spring.cqrs.api.CommandServiceSpec;
import com.byoskill.spring.cqrs.gate.api.CommandExceptionContext;

final class CommandExceptionContextImpl implements CommandExceptionContext {
    private final Object			 command;
    private final Throwable			 e;
    private final CommandServiceSpec<?, ?> handler;

    /**
     * Instantiates a new command exception context implementation.
     *
     * @param command the command
     * @param e the e
     * @param handler the handler
     */
    public CommandExceptionContextImpl(final Object command, final Throwable e,
	    final CommandServiceSpec<?, ?> handler) {
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