/*
 * Copyright (C) 2017 Sylvain Leroy - BYOSkill Company All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the MIT license with
 * this file. If not, please write to: sleroy at byoskill.com, or visit : www.byoskill.com
 *
 */
package com.byoskill.spring.cqrs.gate.impl;

import java.lang.annotation.Annotation;

import com.byoskill.spring.cqrs.api.CommandServiceProvider;
import com.byoskill.spring.cqrs.executors.api.CommandExecutionContext;

public class CommandExecutionContextImpl implements CommandExecutionContext {
    private final CommandServiceProvider handlersProvider;
    private final Object		 command;

    /**
     * Instantiates a new command execution context impl.
     *
     * @param handlersProvider
     *            the handlers provider
     * @param command
     *            the command
     */
    public CommandExecutionContextImpl(final CommandServiceProvider handlersProvider, final Object command) {
	this.handlersProvider = handlersProvider;
	this.command = command;
    }

    @Override
    public <A extends Annotation> A getAnnotation(final Class<A> annotationClass) {
	return command.getClass().getAnnotation(annotationClass);
    }

    @Override
    public <T> T getCommand(final Class<T> impl) {
	return impl.cast(command);
    }

    @Override
    public Object getRawCommand() {
	return command;
    }

}
